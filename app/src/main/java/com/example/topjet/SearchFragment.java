package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public class SearchFragment extends Fragment {

    // Initialise RecyclerView
    RecyclerView rvArts, rvCulture, rvValues;

    private static final String TAG = "SearchFragment"; // create Log.d

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false); // inflate Fragment's view

        // Retrieve the fragment from HomeActivity
        String email = getArguments().getString("email");
        Log.d(TAG, "Search Fragment Email: " + email);

        rvArts = view.findViewById(R.id.rvArts);
        rvCulture = view.findViewById(R.id.rvCulture);
        rvValues = view.findViewById(R.id.rvValues);

        return view;
    } // end of onCreate method
}

