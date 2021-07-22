package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment"; // create Log.d

    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username"; // these are the names for the fields in FireStore
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IDENTIFY = "cbIdentify";

    TextView tvGetProfileEmail, tvGetProfileUsername, tvGetProfilePassword, tvGetProfileIdentify;

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Retrieve the fragment from HomeActivity
        String email = getArguments().getString("email");
        // I feel so silly {again}, i put this behind the return statement and was wondering why this line of code wasn't working.
        Log.d(TAG, "ProfileFragment email is: " + email);

        tvGetProfileEmail = view.findViewById(R.id.tvGetProfileEmail);
        tvGetProfileUsername = view.findViewById(R.id.tvGetProfileUsername);
        tvGetProfilePassword = view.findViewById(R.id.tvGetProfilePassword);
        tvGetProfileIdentify = view.findViewById(R.id.tvGetProfileIdentify);

        // now that we have the username, we want to access Firebase and get all the information relating back to the user.
        retrieveUserInfo(email);

        // TODO - add a button for edit profile info

        return view;
    } // end of onCreate

    // Retrieve user information from FireStore
    public void retrieveUserInfo(String email){

        // Connect to FireStore
        DocumentReference userRef = database.collection("Users").document(email);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    // now  the credentials
                    String keyEmail = documentSnapshot.getString(KEY_EMAIL);
                    String keyUsername = documentSnapshot.getString(KEY_USERNAME);
                    String keyPassword = documentSnapshot.getString(KEY_PASSWORD);
                    String keyIdentify = documentSnapshot.getString(KEY_IDENTIFY);

                    tvGetProfileEmail.setText(keyEmail);
                    tvGetProfileUsername.setText(keyUsername);
                    tvGetProfilePassword.setText(keyPassword);
                    tvGetProfileIdentify.setText(keyIdentify);
                    Log.d(TAG, keyEmail); // check to see whether the code works or not.
                }
            }
        }); // end of FireStore connection



    } // end of retrieveUserInfo method

}

