package com.example.topjet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

// User registers account

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity"; // create Log.d

    // Values for FireStore
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username"; // these are the names for the fields in FireStore
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IDENTIFY = "cbIdentify";
    private static final String KEY_SCORE = "score";


    // Access FireStore database
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference userRef = database.collection("Users");
//    DocumentReference userRef = database.collection("Users").document();

    EditText email, username, passwordOne, passwordTwo;
    CheckBox cbIdentify;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.etEmail);
        username = findViewById(R.id.etUsername);
        passwordOne = findViewById(R.id.etPassword);
        passwordTwo = findViewById(R.id.etPasswordTwo);
        register = findViewById(R.id.btRegister);
        cbIdentify = findViewById(R.id.cbIdentify);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInfo();
            }
        }); // end of setOnClickListener method

    } // end of onCreate method

    private void checkUserInfo(){
        String getEmail = email.getText().toString();
        String getUsername = username.getText().toString();
        String getPassword = passwordOne.getText().toString();
        String getPasswordTwo = passwordTwo.getText().toString();
        String getCheckBox = checkCheckBox(); // will return 'yes' or 'no'

        // if-else statement to see whether all the fields are filled in or not.
        if (!getEmail.isEmpty() && !getUsername.isEmpty() && !getPassword.isEmpty() && !getPasswordTwo.isEmpty()){
            // check whether the username already exists in the database or not
            checkEmail(getEmail, getUsername, getPassword, getPasswordTwo, getCheckBox);
        } else if (getEmail.isEmpty() || getUsername.isEmpty() || getPassword.isEmpty() || getPasswordTwo.isEmpty()) {
            Log.d(TAG, getEmail);
            Toast.makeText(RegisterActivity.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }
        // end of if-else statement
    } // end of checkInfo method


    // TODO - Make sure that the username is also unique in addition to the email
    // Method for checking whether the username already exists in the database
    private void checkEmail(String getEmail, String getUsername, String getPassword, String getPasswordTwo, String getCheckBox) {

        // Check information regarding the particular user
        DocumentReference userRef = database.collection("Users").document(getEmail);
        Log.d(TAG, "print: " + getEmail);

        // Check whether the credentials are correct or incorrect
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    // return email & password
                    String email = documentSnapshot.getString(KEY_EMAIL);
                    if (email.equals(getEmail)){
                        Toast.makeText(RegisterActivity.this, "Sorry, username already exists", Toast.LENGTH_SHORT).show();
                    }
                } else if (!email.equals(getEmail)){
                    if (getPassword.equals(getPasswordTwo)){ // password & passwordTwo must match
                        Log.d(TAG, "All are Filled");
                        registerUser(getEmail, getUsername, getPassword, getCheckBox); // pass the values into the registerUser
                    } else if (getPassword.equals(getPasswordTwo)){
                        Log.d(TAG, "Second one!");
                        Toast.makeText(RegisterActivity.this, "Sorry, username already exists!", Toast.LENGTH_SHORT).show();
                    } else if (!getPassword.equals(getPasswordTwo)){
                        Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Passwords do not match");
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Sorry, there is an error", Toast.LENGTH_SHORT).show();
                } // end of if-else statement
            } // end of onSuccess
        }); // end of addOnSuccessListener method
    }

    private String checkCheckBox(){
        if(cbIdentify.isChecked()){
            Log.d(TAG, "Radio Button is 'Yes'");
            return "Yes";
        } else {
            Log.d(TAG, "Radio Button is 'No'");
            return "No";
        }
    } // end of checkRadioButton method

    // Register the user and insert details into database
    private void registerUser (String getEmail, String getUsername, String getPassword, String getCheckBox){
        // Adding a document
        Map<String, Object> newUser = new HashMap<>();
        newUser.put(KEY_EMAIL, getEmail);
        newUser.put(KEY_USERNAME, getUsername);
        newUser.put(KEY_PASSWORD, getPassword);
        newUser.put(KEY_IDENTIFY, getCheckBox);
        newUser.put(KEY_SCORE, "0");

        // make reference to the first collection
        // to make reference to: database.document("Users/document")
        database.collection("Users").document(getEmail).set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterActivity.this, "User Created!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    } // end of registerUser method

}