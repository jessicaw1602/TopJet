package com.example.topjet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

// handles retrieving the data from FireStore server
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment"; // create Log.d

    private static final String KEY_USERNAME = "username"; // these are the names for the fields in FireStore


    ImageButton btEvents, btDiscussion, btContent;
    TextView tvWelcome;

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate Fragment's view
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btEvents = view.findViewById(R.id.btEvents);
        btDiscussion = view.findViewById(R.id.btDiscussion);
        btContent = view.findViewById(R.id.btContent);
        tvWelcome = view.findViewById(R.id.tvWelcome);

        // Retrieve the fragment from HomeActivity
        String email = getArguments().getString("email");
        Log.d(TAG, "The user's email is: " + email);

        // get the email from the database and return the username and set the Welcome text to the username
        returnUsername(email);

        // When the user clicks on the Content Button, then I want them to create a new fragment to go to the SearchFragment
        btContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btContent Button pressed");
                goToSearchFragment(email);
            }
        }); // end of btContent onClickListener

        btDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDiscussionFragment(email);
            }
        }); // end of btDiscussion.setOnClickListener

        btEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEventsFragment(email);
            }
        }); // end of btDiscussion.setOnClickListener


        return view;
    } // end of onCreateView

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


    // return the username from the user's email
    private void returnUsername (String email){
        // Connect to FireStore
        DocumentReference userRef = database.collection("Users").document(email);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String keyUsername = documentSnapshot.getString(KEY_USERNAME);
                    tvWelcome.setText("Welcome, " + keyUsername);
                }
            }
        });
    } // end of returnUsername method

    private void goToSearchFragment(String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchFragment searchFragment = new SearchFragment(); // generate a new searchFragment

        // send bundle for the Search Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        searchFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, searchFragment); // replace the current frame with searchFragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    } // end of goToSearchFragment method

    private void goToEventsFragment(String email){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GameFragment gameFragment = new GameFragment(); // generate a new searchFragment

        // send bundle for the Game Fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        gameFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, gameFragment); // replace the current frame with gameFragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        System.out.println("gameFragment works!");
    }


}

