package com.example.topjet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ProfileEditFragment extends Fragment {

    private static final String TAG = "ProfileFragment"; // create Log.d

    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username"; // these are the names for the fields in FireStore
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IDENTIFY = "cbIdentify";

    TextView tvEditProfileEmail, tvEditProfileIdentify, tvEditIdentify;
    EditText etProfileUsername, etEditPassword;
    Button btEditProfileEdit;

    String email = null;

    // Access FireStore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        // Retrieve the fragment from Profile Fragment
        email = getArguments().getString("email");
        Log.d(TAG, "ProfileEditFragment email is: " + email);

        // Things that can't be edited
        tvEditProfileEmail = view.findViewById(R.id.tvEditProfileEmail);
//        tvEditProfileIdentify = view.findViewById(R.id.tvEditIdentify);
//        tvEditIdentify = view.findViewById(R.id.tvEditIdentify);

        // Things that we can edit
        etEditPassword = view.findViewById(R.id.etEditPassword);
        etProfileUsername = view.findViewById(R.id.etProfileUsername);
        btEditProfileEdit = view.findViewById(R.id.btEditProfileEdit);

        setHasOptionsMenu(true);

        retrieveUserInfo(email);

        // now we want to be able to retrieve the values from the database
        btEditProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(email); // show alert dialogue and make sure that they want to make changes
            }
        }); // end of btEditProfile.setOnClickListener

        return view;
    } // end of onCreate

    public void retrieveUserInfo(String email){
        // Connect to FireStore
        DocumentReference userRef = database.collection("Users").document(email);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    // now retrieve the credentials
                    String keyEmail = documentSnapshot.getString(KEY_EMAIL);
                    String keyUsername = documentSnapshot.getString(KEY_USERNAME);
                    String keyPassword = documentSnapshot.getString(KEY_PASSWORD);
                    String keyIdentify = documentSnapshot.getString(KEY_IDENTIFY);

                    Log.d(TAG, "username is: " + keyUsername);

                    tvEditProfileEmail.setText(keyEmail);
//                    tvEditProfileIdentify.setText(keyIdentify);
                    etProfileUsername.setText(keyUsername);
                    etEditPassword.setText(keyPassword);
                }
            }
        }); // end of FireStore connection
    } // end of retrieveUserInfo method

    private void showAlertDialog(String email) {
        new AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setMessage("Are you sure you want to make changes?")
                .setPositiveButton("OK PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToProfileFragment(email); // Go back to Profile Fragment
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Stay on the page
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    } // end of showAlertDialog

    private void goToProfileFragment(String email) {

        String getUsernameEdit = etProfileUsername.getText().toString();
        String getPasswordEdit = etEditPassword.getText().toString();

        // now make the changes to the database
        database.collection("Users").document(email).update(KEY_USERNAME, getUsernameEdit);
        database.collection("Users").document(email).update(KEY_PASSWORD, getPasswordEdit);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment(); // generate a new searchFragment

        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        profileFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_frame, profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    } // end of goToProfileFragment method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showAlertDialog(email);
                // Navigate to settings screen
                // getActivity().onBackPressed();
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
