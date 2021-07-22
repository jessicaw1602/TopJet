package com.example.topjet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* Code adapted from: https://www.youtube.com/watch?v=pjFcJ6EB8Dg &&
https://developers.google.com/maps/documentation/places/web-service/search?hl=en_GB */

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";

    Spinner mapSpinner;
    Button btMapSearch;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    FusedLocationProviderClient locationProvider;
    double currentLatitude = 0;
    double currentLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapSpinner = findViewById(R.id.mapSpinner);
        btMapSearch = findViewById(R.id.btnMapSearch);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        // Set the Spinner Adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, getResources().getStringArray(R.array.MapLocations));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(spinnerAdapter);

        // initialise fused client location provider
        locationProvider = LocationServices.getFusedLocationProviderClient(this);

        // Check Permission and get GeoLocation
        getGeoLocation();

        // Produce the
        btMapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the text of the spinner
                String getTag = mapSpinner.getSelectedItem().toString();
                // i might have to replace the spaces with '+'
                String searchTag = getTag.replaceAll(" ", "+").toLowerCase();

                // now produce the google search
                String mapUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                        + "location=" + currentLatitude + "," + currentLongitude + "&radius=1500"
                        + "&type=art_gallery" // will return the art_gallery places https://developers.google.com/maps/documentation/places/web-service/supported_types
                        // + "&sensor=true"
                        + "&keyword=aboriginal"
                        + "&key=" + getResources().getString(R.string.google_place_key);

                /*
                https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=art_gallery&keyword=aboriginal&key=AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y
                 */
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());

                // execute place task to download json data

                // new PlaceTask().execute(mapUrl)
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Background work here
                        String data = null;
                        try {
                            data = downloadUrl(mapUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });

                    }
                });
            }
        }); // end of btMapSearch.setOnClickListener

    } // end of onCreate




    private String downloadUrl(String string) throws IOException {
        InputStream stream = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(string); // will be an empty string
            conn = (HttpURLConnection) url.openConnection(); // prepare the HTTP connection
            conn.connect();

            stream = conn.getInputStream(); // initialise input stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream)); // buffer the text input
            StringBuilder builder = new StringBuilder(); // initialise the string builder

            String line = ""; // initialise string variable

            while((line = reader.readLine()) !=null){
                builder.append(line); // append String
            }
            // get appended data
            String data = builder.toString();
            reader.close();

            return data;

        } catch (IOException e){
            Log.d(TAG, "Sorry there is an error!");
            return null;
        }

    } // end of download Url

    private void getGeoLocation() {
        // Check Permission
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            // get task location
            Task<Location> taskLocation = locationProvider.getLastLocation();
            taskLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();

                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                mMap = googleMap; // set the location on the google map

                                // Zoom into map
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 15));
                            }
                        }); // end of mapFragment.getMapAsync
                    } else {
                        Toast.makeText(MapActivity.this, "Failed to Get Location. Please Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }); // end of taskLocation.addOnSuccessListener

        } else { // if denied, request permission
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            getGeoLocation();
        } // end of if-else for permission


    } // end of getGeoLocation method

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getGeoLocation();
            }
        }
    } // end of onRequestPermissionsResult

}