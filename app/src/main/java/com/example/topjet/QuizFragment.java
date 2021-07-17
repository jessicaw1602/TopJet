package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    private static final String KEY_USERNAME = "username";

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate Fragment's view
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Retrieve bundles
        String email = getArguments().getString("email");
        Log.d(TAG, "The user's email is: " + email);

        // I want to be able to receive the topicArea's text from ViewContentFragment, so i can retrieve the correct Quiz questions
        //TODO - finish this off -> from ViewContentFragment
        String topicAreas = getArguments().getString("topicArea");
        Log.d(TAG, "Quiz topicArea is: " + topicAreas);


        return view;
    } // end of onCreateView
}