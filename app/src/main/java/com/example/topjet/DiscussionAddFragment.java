package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.topjet.Entities.CommentEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DiscussionAddFragment extends Fragment {

    private static final String TAG = "DiscussionAddFragment"; // create Log.d

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private static final String KEY_TITLE = "title";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DATE = "date";
    private static final String KEY_POST_TAG = "postTag";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SHORT_DESC = "shortDesc";
    private static final String KEY_ID = "docId";

    private static final String KEY_SCORE = "score"; // to update the user's score

    EditText etTitleName, etNewPostContent;
    Button btPost, btDiscardPost;
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussionadd, container, false);

        // Retrieve the fragment from HomeActivity
        String email = getArguments().getString("email");
        Log.d(TAG, "DiscussionFragment email is: " + email);

        etTitleName = view.findViewById(R.id.etTitleName);
        etNewPostContent = view.findViewById(R.id.etNewPostContent);
        btPost = view.findViewById(R.id.btPost);
        btDiscardPost = view.findViewById(R.id.btDiscardPost);
        spinner = view.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Tags));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        btDiscardPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to Discussion Fragment
                goToDiscussionFragment(email);
            }
        }); // end of btDiscardPost.setOnClickListener

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // grab all the info and save it into the database
                createPost(email);
            }
        });

        return view;
    } // end of onCreate


    // Save the post in the database
    private void createPost(String email){

        // return the username of the user
        DocumentReference userRef = database.collection("Users").document(email);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    // return the username
                    String username = documentSnapshot.getString(KEY_USERNAME);
                    String userScore = documentSnapshot.getString(KEY_SCORE);
                    Log.d(TAG, "UserScore is: " + userScore);

                    // Retrieve the text from the xml file
                    String titleName = etTitleName.getText().toString();
                    String postContent = etNewPostContent.getText().toString();
                    String getTag = spinner.getSelectedItem().toString();
                    Log.d(TAG, getTag);

                    // Check if the string is >= or <= 100 char
                    if (postContent.length() <= 100){
                        String shortDesc = (etNewPostContent.getText().toString()).substring(0,postContent.length()); // only get whatever text is available
                        addPost(titleName, postContent, getTag, username, shortDesc, email, userScore);
                    } else if (postContent.length() >= 100){
                        String desc = (etNewPostContent.getText().toString()).substring(0,100); // cut the OG postContent into 100 chars
                        String shortDesc = desc + "...";

                        addPost(titleName, postContent, getTag, username, shortDesc, email, userScore);
                    }
                } else {
                    Toast.makeText(getContext(), "Error! Please Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    } // end of the createPost method

    // addPost
    private void addPost(String titleName, String postContent, String getTag, String username, String shortDesc, String email, String userScore){
        if (!titleName.isEmpty() && !postContent.isEmpty()){
            // get the date & time
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String getDate = formatter.format(date);
            Log.d(TAG, "The current date is: " + getDate);

            // Create a new Collection and document path
            DocumentReference postRef = database.collection("Posts").document();
            String docId = postRef.getId();

            // Create the new Blog Post with the username
            Map<String, Object> newPost = new HashMap<>();
            newPost.put(KEY_TITLE, titleName);
            newPost.put(KEY_USERNAME, username);
            newPost.put(KEY_DATE, getDate);
            newPost.put(KEY_POST_TAG, getTag);
            newPost.put(KEY_SHORT_DESC, shortDesc);
            newPost.put(KEY_CONTENT, postContent);
            newPost.put(KEY_ID, docId);

            // Add the blog post into the database
            database.collection("Posts").document(docId).set(newPost)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // go back to the Discussion page

                            goToDiscussionFragment(email);
                            Toast.makeText(getActivity(), "Post Created!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Created new Post");

                            // Now we want to update the user's score.
                            int userScores = Integer.parseInt(userScore);
                            int updateUserScore = userScores + 5;
                            String updateScore = String.valueOf(updateUserScore);
                            Log.d(TAG, "Updated score is: " + updateScore);
                            database.collection("Users").document(email).update(KEY_SCORE, updateScore);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Sorry, there was an Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });
        } else {
            Toast.makeText(getContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    // go to the discussion page
    private void goToDiscussionFragment(String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DiscussionFragment discussionFragment = new DiscussionFragment(); // generate a new searchFragment

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        discussionFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, discussionFragment); // replace the current frame with searchFragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    } // end of goToDiscussionFragment

}
