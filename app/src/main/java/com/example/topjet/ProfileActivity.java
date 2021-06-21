package com.example.topjet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // set the initial Fragment Frame (from activity_home.xml) to HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, new ProfileFragment()).commit();

    } // end of onCreate method

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // initially set the selected fragment to null
                    Fragment selectedFragment = null;

                    // use a switch statement to determine which page the user is on by id
                    switch (item.getItemId()){
                        case R.id.homeFragment:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.searchFragment:
                            selectedFragment = new SearchFragment();
                            break;

                        case R.id.profileFragment:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, selectedFragment).commit();

                    return true;
                }
            }; // end of OnNavigationItemSelectedListener


}