package com.example.topjet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

// This will act as the Login Page

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // create Log.d
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERNAME = "username";


    EditText email, password;
    Button login, signup;

    // Access FireStore database
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference userRef = database.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        login = findViewById(R.id.btRegister);
        signup = findViewById(R.id.btSignUp);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = email.getText().toString();
                String getPassword = password.getText().toString();

                userLogin(getEmail, getPassword);
                Log.d(TAG, "login button pressed");

            }
        }); // end of login.setOnClickListener

        // method if the user wants to signup
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }); // end of signup.setOnClickListener method


    } // end of onCreate

    // Method for sign-in
    private void userLogin(String getEmail, String getPassword) {

        // Check information regarding the particular user
        DocumentReference userRef = database.collection("Users").document(getEmail);

        // Check whether the fields are empty or not
        if (!getEmail.isEmpty() && !getPassword.isEmpty()) {
            Log.d(TAG, "print: " + getEmail);

            // Check whether the credentials are correct or incorrect
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        // return email & password
                        String email = documentSnapshot.getString(KEY_EMAIL);
                        String password = documentSnapshot.getString(KEY_PASSWORD);

                        if (email.equals(getEmail) && password.equals(getPassword)){
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra(HomeActivity.INTENT_EMAIL, email); // pass the email to HomeActivity
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                    } // end of if-else statement
                }
            }); // end of addOnSuccessListener method
        } else {
            Toast.makeText(MainActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
        } // end of if-else
    } // end of userLogin method

        /*
        private void openActivityDetail(String message){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.INTENT_MESSAGE, message); // if you want to add in extra info
            startActivity(intent);
        }
         */
}