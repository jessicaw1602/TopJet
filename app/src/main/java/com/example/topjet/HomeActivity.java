 package com.example.topjet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Navigation Menu Bar code is derived from: https://www.youtube.com/watch?v=tPV8xA7m-iw

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavListener); // i think this will colour the chosen fragment.

        // set the initial Fragment Frame (from activity_home.xml) to HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, new HomeFragment()).commit();

    } // end of onCreate method

    // Check to see which bottom menu item the user is on.
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // initially set the selected fragment to null
            Fragment selectedFragment = null;

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

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, selectedFragment).commit();

            return true;
        }
    }; // end of OnNavigationItemSelectedListener

}