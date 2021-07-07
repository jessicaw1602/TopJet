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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // we want to connect to the database to retrieve the title and the other information
        DocumentReference postRef = database.collection("Posts").document(title);
        postRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    // return all the elements
                    String detailTitle = documentSnapshot.getString(KEY_TITLE);
                    String detailUsername = documentSnapshot.getString(KEY_USERNAME);
                    String detailDate = documentSnapshot.getString(KEY_DATE);
                    String detailPostTag = documentSnapshot.getString(KEY_POST_TAG);
                    String detailContent = documentSnapshot.getString(KEY_CONTENT);

                    // now set the TextView elements
                    tvDetailTitle.setText(detailTitle);
                    tvDetailUserNDate.setText(detailUsername + " | " + detailDate);
                    tvDetailPostTag.setText(detailPostTag);
                    tvDetailContent.setText(detailContent);

                    Log.d(TAG, "Database Title: " + detailTitle);
                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Unable to retrieve data");
                }
            }
        }); // end of postRef.addOnSuccessListener


        return view;
    } // end of onCreate
}
