 package com.example.topjet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Navigation Menu Bar code is adapted from: https://www.youtube.com/watch?v=tPV8xA7m-iw

public class HomeActivity extends AppCompatActivity {

    public static final String INTENT_EMAIL = "com.example.topjet.intent_email";

    private static final String TAG = "HomeActivity"; // create Log.d

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                checkMenuItem(item);
                return true;
            }
        });
        // Retrieve the username's intent from MainActivity
        Intent intent = getIntent();
        String email = intent.getStringExtra(INTENT_EMAIL);

        Fragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        homeFragment.setArguments(bundle);

        // set the initial Fragment Frame (from activity_home.xml) to HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, homeFragment).commit();
//         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, new HomeFragment()).commit();

    } // end of onCreate method

    // Create a method to check which menu item the user is on.
    private void checkMenuItem(MenuItem item){
        Fragment selectedFragment = null; // initially set the selected fragment to null

        // Retrieve the username's intent from MainActivity
        Intent intent = getIntent();
        String email = intent.getStringExtra(INTENT_EMAIL); // return the name of the user

        Bundle bundle = new Bundle();
        bundle.putString("email", email); // pass the username into the bundle

        // use a switch statement to check which page the user is on, by id
        switch (item.getItemId()){
            case R.id.homeButton:
                selectedFragment = new HomeFragment();
                break;

            case R.id.searchButton:
                selectedFragment = new SearchFragment();
                break;

            case R.id.profileButton:
                selectedFragment = new ProfileFragment();
                break;
        }

        Log.d(TAG, String.valueOf(selectedFragment));
        selectedFragment.setArguments(bundle); // for the new fragment, set the arguments to username
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, selectedFragment).commit();

    } // end of checkMenuItem method

}