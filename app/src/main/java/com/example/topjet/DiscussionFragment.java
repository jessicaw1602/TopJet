package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topjet.Entities.CommentEntity;
import com.example.topjet.Entities.DiscussionEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*  Filtered Query code is adapted from: https://www.tutorialsbuzz.com/2020/09/android-filter-recyclerview-searchView-java.html */
public class DiscussionFragment extends Fragment implements SearchView.OnQueryTextListener{

    private DiscussionAdapter discussionAdapter;
    private List<DiscussionEntity> discussionList;
    private List<DiscussionEntity> filteredList;

    private static final String TAG = "DiscussionFragment"; // create Log.d

    // Access FireStore database
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference postRef = database.collection("Posts");

    // Values for FireStore
    private static final String KEY_TITLE = "title";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DATE = "date";
    private static final String KEY_POST_TAG = "postTag";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SHORT_DESC = "shortDesc";
    private static final String KEY_ID = "docId";
    private static final String KEY_COMMENT = "comment";

    Button btAddPost;
    RecyclerView rvDiscussionPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussion, container, false);

        // Retrieve the fragment from HomeActivity
        String email = getArguments().getString("email");
        Log.d(TAG, "Discussion Fragment Email: " + email);

        createDummyPost(email);

        btAddPost = view.findViewById(R.id.btAddPost);
        rvDiscussionPosts = view.findViewById(R.id.rvDiscussionPosts);

        btAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPost(email);
            }
        }); // end of onClickListener

        rvDiscussionPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDiscussionPosts.setHasFixedSize(true);
        discussionList = new ArrayList<DiscussionEntity>();
        filteredList = new ArrayList<DiscussionEntity>();
        filteredList = discussionList; // set the filtered list to the discussionList

        //Action bar
        setHasOptionsMenu(true);

        DiscussionAdapter.RecyclerViewClickListener listener = new DiscussionAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, String title) {
                Log.d(TAG, "DiscussionFragment title: " + title);
                openDetailActivity(title, email);
            }
        }; // end of DiscussionAdapter.RecyclerViewClickListener

        retrieveAllPosts();

        discussionAdapter = new DiscussionAdapter(discussionList, listener);

        return view;
    } // end of onCreate

    private void openDetailActivity(String title, String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DiscussionDetailFragment disDetailFragment = new DiscussionDetailFragment();

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("postId", title);
        disDetailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, disDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Go to another fragment
    private void addNewPost(String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DiscussionAddFragment discussionAddFragment = new DiscussionAddFragment(); // generate a new searchFragment

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        discussionAddFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, discussionAddFragment); // replace the current frame with searchFragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void retrieveAllPosts(){
        // Create a query to retrieve all the values from the database
        Query returnAllPosts = database
                .collection("Posts")
                .orderBy("date", Query.Direction.DESCENDING);
        returnAllPosts.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        discussionList.add(doc.getDocument().toObject(DiscussionEntity.class));

                    }
                    discussionAdapter.notifyDataSetChanged();
                    rvDiscussionPosts.setAdapter(discussionAdapter);
                }
            }
        }); // end of returnAllPosts.addSnapshot Listener

    } // end of retrieveAllPosts method

    // Inserting the dummy data in
    private void createDummyPost(String userEmail){
        DocumentReference userRef = database.collection("Users").document(userEmail);
        Log.d(TAG, "print from DiscussionFragment: " + userEmail);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // return email & password
                    String username = documentSnapshot.getString(KEY_USERNAME);

//                    DocumentReference postRef = database.collection("Posts").document();
//                    String docId = postRef.getId();

                    // Create the new Blog Post with the username
                    Map<String, Object> newPost = new HashMap<>();
                    newPost.put(KEY_TITLE, "My Story");
                    newPost.put(KEY_USERNAME, username);
                    newPost.put(KEY_DATE, "16/02/2021 18:28:18");
                    newPost.put(KEY_POST_TAG, "Stories");
                    newPost.put(KEY_SHORT_DESC, "This is the short description");
                    newPost.put(KEY_CONTENT, "My Story is about my childhood. This will be longgggggggggggggggggggggggggggggg sdfasfdasfsadfdasfsadfasfdsfadsfsas.");
                    newPost.put(KEY_ID, "fPlnYrsTnkLq2zK7pAkG");

                    database.collection("Posts").document("fPlnYrsTnkLq2zK7pAkG").set(newPost)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Created new Dummy Post");

                                    // Create a new Comment
                                    CommentEntity commentEntity = new CommentEntity("tAFzIxIHk6mV1YqMWuIV", "fPlnYrsTnkLq2zK7pAkG", username, "08/07/2021 18:20:50", "I really enjoyed your post. Thank you for sharing!");

                                    CollectionReference comments = database.collection("fPlnYrsTnkLq2zK7pAkG");
                                    comments.document("tAFzIxIHk6mV1YqMWuIV").set(commentEntity);

//                                    database.collection("Posts").document("fPlnYrsTnkLq2zK7pAkG")
//                                            .collection("Comments").document("tAFzIxIHk6mV1YqMWuIV").set(commentEntity);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });
                }
            } // end of onSuccess
        }); // end of addOnSuccessListener method
    } // end of createDummyPost method

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.search_menu, menu); // inflate the menu bar
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // If the menu item is collapsed
                discussionAdapter.setFilter(filteredList);

                return true;
            }
        }); // end of item.setOnActionExpandListener


    } // end of onCreateOptionsMenu


    //Action bar back button + sort by in menu option
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.fragment_frame:
                return true;
            case R.id.sortByOldestToNewest:
                discussionAdapter.sort(DiscussionAdapter.SORT_METHOD_BY_NEWEST);
                return true;
            case R.id.sortByNewestToOldest:
                discussionAdapter.sort(DiscussionAdapter.SORT_METHOD_BY_OLDEST);
                return true;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<DiscussionEntity> discussionFiltered = filter(filteredList, newText);
        discussionAdapter.setFilter(discussionFiltered);
        return true;
    } // end of onQueryTextChange

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<DiscussionEntity> filter (List<DiscussionEntity> filteredModel, String query){
        query = query.toLowerCase(); // query = user input

        final List<DiscussionEntity> discussionFiltered = new ArrayList<>();
        for (DiscussionEntity discussionFilter: filteredModel){

            String textOne = discussionFilter.getTitle().toLowerCase(); // this will need to be filtered by date
            String textTwo = discussionFilter.getPostTag().toLowerCase();
            String textThree = discussionFilter.getShortDesc().toLowerCase();
            String textFour = discussionFilter.getUsername().toLowerCase();

            if (textOne.contains(query) || textTwo.contains(query) || textThree.contains(query) || textFour.contains(query)){
                discussionFiltered.add(discussionFilter);
            }
        }
        return discussionFiltered;
    } // end of filter method
}


/*
// Use this code for creating a new comment
// This code is used to show How to create a new sub-collection of a post & then save the docId as the new document's Id

                                    DocumentReference commentRef = database.collection("Posts").document("fPlnYrsTnkLq2zK7pAkG")
                                            .collection("Comments").document();

                                    String docId = commentRef.getId();
                                    Log.d(TAG, "The docID of the commentRef is: " + docId);

                                    CommentEntity commentEntity = new CommentEntity(docId, "fPlnYrsTnkLq2zK7pAkG", username, "08/07/2021 18:20:50", "I really enjoyed your post. Thank you for sharing!");

                                    database.collection("Posts").document("fPlnYrsTnkLq2zK7pAkG")
                                            .collection("Comments").document(docId).set(commentEntity);

                                }
 */


/* // Creating User query
                database.collection("Posts")
                        .whereEqualTo(KEY_USERNAME, userEmail)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    // then we want to create a new
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // return email & password
                                        String email = document.getString(KEY_EMAIL);
                                        Log.d(TAG, "The email from FireStore is: " + email);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());

                                }
                            }
                        }); // end of query
 */
