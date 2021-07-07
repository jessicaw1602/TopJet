package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class DiscussionFragment extends Fragment {

    private DiscussionAdapter discussionAdapter;
    private List<DiscussionEntity> discussionList;

    private static final String TAG = "DiscussionFragment"; // create Log.d

    // Access FireStore database
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference userRef = database.collection("Users");

    // Values for FireStore
    private static final String KEY_TITLE = "title";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DATE = "date";
    private static final String KEY_POST_TAG = "postTag";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SHORT_DESC = "shortDesc";

    EditText etSearchPosts;
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

        //TODO - Add comments

        etSearchPosts = view.findViewById(R.id.etSearchPosts);
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

        DiscussionAdapter.RecyclerViewClickListener listener = new DiscussionAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, String title) {
                //TODO - go to the full page including the detailed description
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
        DiscussionDetailFragment disDetailFragment = new DiscussionDetailFragment(); // generate a new searchFragment

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("title", title);
        disDetailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, disDetailFragment); // replace the current frame with searchFragment
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

                    // Create the new Blog Post with the username
                    Map<String, Object> newPost = new HashMap<>();
                    newPost.put(KEY_TITLE, "My Story");
                    newPost.put(KEY_USERNAME, username);
                    newPost.put(KEY_DATE, "16-Feb-2021");
                    newPost.put(KEY_POST_TAG, "Stories");
                    newPost.put(KEY_SHORT_DESC, "This is the short description");
                    newPost.put(KEY_CONTENT, "My Story is about my childhood. This will be longgggggggggggggggggggggggggggggg sdfasfdasfsadfdasfsadfasfdsfadsfsas.");

                    database.collection("Posts").document("My Story | " + username).set(newPost)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Created new Dummy Post");
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


}

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
