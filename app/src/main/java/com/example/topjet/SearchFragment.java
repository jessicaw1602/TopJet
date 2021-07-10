package com.example.topjet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public class SearchFragment extends Fragment {

    // Initialise RecyclerView
    RecyclerView rvArts, rvCulture, rvValues;

    //TODO - delete later
    Button btArts;

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

        //TODO - delete this later - this will show the Arts > Symbol info...
        btArts = view.findViewById(R.id.btArts);
        btArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Bundle
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ViewContentFragment viewContent = new ViewContentFragment();

                // send bundle for the Search Fragment
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                viewContent.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_frame, viewContent);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }); // end of setOnClickListener


        return view;
    } // end of onCreate method

}

