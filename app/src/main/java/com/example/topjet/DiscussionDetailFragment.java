package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.topjet.Entities.DiscussionEntity;
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

public class DiscussionDetailFragment extends Fragment {

    private static final String TAG = "DetailFragment"; // create Log.d

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    TextView tvDetailTitle, tvDetailUserNDate, tvDetailPostTag, tvDetailContent;

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

        String title = getArguments().getString("title");
        Log.d(TAG, "docId received: " + title);

        tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
        tvDetailUserNDate = view.findViewById(R.id.tvDetailUserNDate);
        tvDetailPostTag = view.findViewById(R.id.tvDetailPostTag);
        tvDetailContent = view.findViewById(R.id.tvDetailContent);

        // Check whether this would work
        DocumentReference postRefs = database.collection("Posts").document(title);
        postRefs.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    // return all the elements
                    String id = documentSnapshot.getString(KEY_ID);
                    getData(id);
                    Log.d(TAG, "ID DetailFragment: " + id);
                } else {
                    Log.d(TAG, "Unable to retrieve data");
                }
            }
        }); // end of postRef.addOnSuccessListener

        return view;
    } // end of onCreate

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

