package com.example.topjet;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topjet.Entities.CommentEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// User will be able to view all the comments & post a comment
public class CommentFragment extends Fragment {

    private static final String TAG = "CommentFragment";

    EditText etWritePost;
    RecyclerView rvShowComments;
    Button btPostComment;

    private CommentAdapter commentAdapter;
    private List<CommentEntity> commentList;

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private static final String KEY_TITLE = "title";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DATE = "date";
    private static final String KEY_DOC_ID = "docId";
    private static final String KEY_POST_ID = "postId";
    private static final String KEY_COMMENT = "comment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate Fragment's view
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        // Retrieve the fragment from DiscussionDetailFragment
        String email = getArguments().getString("email");
        Log.d(TAG, "DetailFragment email is: " + email);

        String postId = getArguments().getString("postId");
        Log.d(TAG, "postId received: " + postId);

        etWritePost = view.findViewById(R.id.etWritePost);
        btPostComment = view.findViewById(R.id.btPostComment);
        rvShowComments = view.findViewById(R.id.rvShowComments);

        rvShowComments.setHasFixedSize(true);
        rvShowComments.setLayoutManager(new LinearLayoutManager(getContext()));
        commentList = new ArrayList<CommentEntity>();
        commentAdapter = new CommentAdapter(commentList);

 // reverse the order of the comment list

        //enable action bar
        setHasOptionsMenu(true);

        // Show all the recyclerview comments
        retrieveComment(postId);

        // now we want to be able to add the new comment when the button is pressed
        btPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment(postId, email);
            }
        });

        return view;
    } // end of onCreateView

    private void addComment(String postId, String email){
        Log.d(TAG, "docId in addComment method: " + postId);

        String getPostText = etWritePost.getText().toString();

        // First check whether the etWritePost is empty or not
        if (!getPostText.isEmpty()){

            // Get the username of the user
            DocumentReference userRef = database.collection("Users").document(email);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        // return the username
                        String username = documentSnapshot.getString(KEY_USERNAME);

                        // get the date & time
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date date = new Date();
                        String getDate = formatter.format(date);
                        Log.d(TAG, "The current date is: " + getDate);

                        // We want to create a new document
                        DocumentReference postRef = database.collection(postId).document();
                        String docId = postRef.getId();

                        // Create the new Blog Post with the username
                        Map<String, Object> newComment = new HashMap<>();
                        newComment.put(KEY_USERNAME, username);
                        newComment.put(KEY_DATE, getDate);
                        newComment.put(KEY_COMMENT, getPostText);
                        newComment.put(KEY_POST_ID, postId);
                        newComment.put(KEY_DOC_ID, docId);

                        // now add it into the database
                        CollectionReference comments = database.collection(postId);
                        comments.document(docId).set(newComment);

                        Toast.makeText(getContext(), "Posted!", Toast.LENGTH_SHORT).show();

                        goToDiscussionDetailFragment(email, postId);

                    } else {
                        Toast.makeText(getContext(), "Error! Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(getContext(), "Error! Please fill in the space!", Toast.LENGTH_SHORT).show();
        }

    } // end of addComment method

    private void goToDiscussionDetailFragment(String email, String postId){
        Log.d(TAG, "The post id that will be sent to goToDetailPage is: " + postId);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DiscussionDetailFragment detailFragment = new DiscussionDetailFragment();

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("postId", postId);
        detailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    } // end of goToDiscussionFragment method

    // Retrieve only the posts that are from the document.
    private void retrieveComment(String postDocId){
        Log.d(TAG, "docId returned: " + postDocId);

        database.collectionGroup(postDocId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                // convert data into an object
                                Map<String, Object> commentMap = document.getData();

                                String docId = document.getId();
                                String username = commentMap.get("username").toString();
                                String date = commentMap.get("date").toString();
                                String comment = commentMap.get("comment").toString();
                                String postId = commentMap.get("postId").toString();
                                commentList.add(new CommentEntity(docId, postId, username, date, comment));

                                if (!commentList.isEmpty()){
                                    commentList.sort(new CommentSorter());
//                                    Collections.sort(commentList,Collections.reverseOrder());
                                } else {

                                }
                            }

                        } else {
                            Log.d(TAG, "Unsuccessful in Retrieving Data");
                        }
                        commentAdapter.notifyDataSetChanged();
                        rvShowComments.setAdapter(commentAdapter);
                    }
                }); // end of returnComments.get()

    } // end of retrieveAllPosts method
    //Action bar back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();

                break;
            case R.id.fragment_frame:

                return true;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
