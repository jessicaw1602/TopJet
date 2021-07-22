package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiscussionDetailFragment extends Fragment {

    private static final String TAG = "DetailFragment"; // create Log.d

    private RecyclerView rvShowComment;
    private CommentAdapter commentAdapter;
    private List<CommentEntity> commentList;

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    TextView tvDetailTitle, tvDetailUserNDate, tvDetailPostTag, tvDetailContent;
    Button btAddComment;

    private static final String KEY_TITLE = "title";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DATE = "date";
    private static final String KEY_POST_TAG = "postTag";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_ID = "docId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussion_detail, container, false);

        // Retrieve the fragment from DiscussionFragment
        String email = getArguments().getString("email");
        Log.d(TAG, "DetailFragment email is: " + email);

        String postId = getArguments().getString("postId");
        Log.d(TAG, "postId received: " + postId);

        tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
        tvDetailUserNDate = view.findViewById(R.id.tvDetailUserNDate);
        tvDetailPostTag = view.findViewById(R.id.tvDetailPostTag);
        tvDetailContent = view.findViewById(R.id.tvDetailContent);
        btAddComment = view.findViewById(R.id.btAddComment);

        rvShowComment = view.findViewById(R.id.rvShowComments);
        rvShowComment.setHasFixedSize(true);
        rvShowComment.setLayoutManager(new LinearLayoutManager(getContext()));
        commentList = new ArrayList<CommentEntity>();
        commentAdapter = new CommentAdapter(commentList);

        // Retrieve the title and get the docId of the post.
        DocumentReference postRefs = database.collection("Posts").document(postId);
        postRefs.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    // return the docId of the post using the title of the post
                    String id = documentSnapshot.getString(KEY_ID);
                    Log.d(TAG, "ID DetailFragment: " + id);

                    getData(postId); // query the documents based on the docId

                    // now connect to the database to retrieve the comments of the specific post
                    retrieveComment(postId);

                } else {
                    Log.d(TAG, "Unable to retrieve data");
                }
            }
        }); // end of postRef.addOnSuccessListener

        btAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference postRefs = database.collection("Posts").document(postId);
                postRefs.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            // return the docId of the post using the title of the post
                            String id = documentSnapshot.getString(KEY_ID);
                            goToCommentFragment(email, id); // pass in the id
                        } else {
                            Log.d(TAG, "Unable to retrieve data");
                        }
                    }
                }); // end of postRef.addOnSuccessListener

            }
        });


        return view;
    } // end of onCreate


    private void goToCommentFragment(String email, String postId){
        Log.d(TAG, "Post id in goToCommentFragment method is: " + postId);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CommentFragment commentFragment = new CommentFragment();

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("postId", postId);
        commentFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, commentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Retrieve only the posts that are from the document.
    //TODO - change this - so that it is only a TextView... NOT A RECYCLERVIEW
    private void retrieveComment(String postDocId){
        Log.d(TAG, "docId returned: " + postDocId);

//        Query returnComments = database.collection("Posts").document(postDocId)
//                .collection("Comments").whereEqualTo("titleId", postDocId);
        database.collectionGroup(postDocId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                        // TODO - we only want to return 1 comment, and then provide a button at
                        for(int i = 0; i < (commentList.size() - (commentList.size() - 1)); i++){
                            commentList.add(new CommentEntity(docId, postId, username, date, comment));
                        }
                    }
                } else {
                    Log.d(TAG, "Unsuccessful in Retrieving Data");
                }
                commentAdapter.notifyDataSetChanged();
                rvShowComment.setAdapter(commentAdapter);
            }
        }); // end of returnComments.get()

    } // end of retrieveAllPosts method

    // Code adapted from: https://stackoverflow.com/questions/46706433/firebase-firestore-get-data-from-collection
    private void getData(String id){
        CollectionReference postsRef = database.collection("Posts");

        Query nameQuery = postsRef.whereEqualTo("docId", id);
        nameQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange docChange : value.getDocumentChanges()){
                    // return all the elements
                    String detailTitle = docChange.getDocument().getData().get(KEY_TITLE).toString();
                    String detailUsername = docChange.getDocument().getData().get(KEY_USERNAME).toString();
                    String detailDate = docChange.getDocument().getData().get(KEY_DATE).toString();
                    String detailPostTag = docChange.getDocument().getData().get(KEY_POST_TAG).toString();
                    String detailContent = docChange.getDocument().getData().get(KEY_CONTENT).toString();

                    // now set the TextView elements
                    tvDetailTitle.setText(detailTitle);
                    tvDetailUserNDate.setText(detailUsername + " | " + detailDate);
                    tvDetailPostTag.setText(detailPostTag);
                    tvDetailContent.setText(detailContent);

                    Log.d(TAG, "Database Title: " + detailTitle);
                }
            }
        }); // end of nameQuery.addSnapshotListener

    }
}

