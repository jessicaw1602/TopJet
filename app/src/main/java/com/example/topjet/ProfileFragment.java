package com.example.topjet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment"; // create Log.d

    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username"; // these are the names for the fields in FireStore
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IDENTIFY = "cbIdentify";
    private static final String KEY_SCORE = "score";


    TextView tvGetProfileEmail, tvGetProfileUsername, tvGetProfilePassword, tvGetProfileIdentify;
    ProgressBar profileProgress;
    TextView tvPercentageDone;
    Button btEditProfile;
    ImageView flagIdentify;

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
        //tvGetProfilePassword = view.findViewById(R.id.tvGetProfilePassword);
        tvGetProfileIdentify = view.findViewById(R.id.tvEditIdentify);
        btEditProfile = view.findViewById(R.id.btEditProfileEdit);
        flagIdentify = view.findViewById(R.id.firstNationIdentify);

        //Progress Bar
        profileProgress = view.findViewById(R.id.profileProgress);
        tvPercentageDone = view.findViewById(R.id.tvPercentageDone);

        setHasOptionsMenu(true);

        // now that we have the username, we want to access Firebase and get all the information relating back to the user.
        retrieveUserInfo(email);

        btEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfile(email);
            }
        }); // end of btEditProfile.setOnClickListener

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
                    String keyScore = documentSnapshot.getString(KEY_SCORE);

                    tvGetProfileEmail.setText(keyEmail);
                    tvGetProfileUsername.setText(keyUsername);
//                  tvGetProfilePassword.setText("********");
                    if (keyIdentify.equals("No")) {
                        flagIdentify.setImageResource(R.drawable.australia);
                    } else {
                        flagIdentify.setImageResource((R.drawable.aborigin));
                    }
                    tvGetProfileIdentify.setText(keyIdentify);
                    tvPercentageDone.setText(keyScore + "/100");

                    // Set the values of the progress bar
                    profileProgress.setMax(100);
                    profileProgress.setProgress(Integer.parseInt(keyScore));

                    Log.d(TAG, keyEmail); // check to see whether the code works or not.
                }
            }
        }); // end of FireStore connection
    } // end of retrieveUserInfo method

    private void goToEditProfile(String email) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProfileEditFragment profileEditFragment = new ProfileEditFragment(); // generate a new searchFragment

        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        profileEditFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, profileEditFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }// end of goToEditProfile method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                // Navigate to settings screen
                break;
            case R.id.fragment_frame:
                // Save profile changes
                return true;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}

