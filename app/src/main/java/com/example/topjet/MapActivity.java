package com.example.topjet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.topjet.PlacesApi.PlacesModel;
import com.example.topjet.PlacesApi.PlacesResponse;
import com.example.topjet.PlacesApi.PlacesRetrofit;
import com.example.topjet.PlacesApi.PlacesService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* Code adapted from: https://www.youtube.com/watch?v=5QkB1-ln8H0&t=141s &&
https://developers.google.com/maps/documentation/places/web-service/search?hl=en_GB &&
https://developers.google.com/maps/documentation/android-sdk/start */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
//    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=art_gallery&keyword=aboriginal&key=AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y";

    Spinner mapSpinner;
    Button btMapSearch;

    // Main elements for the maps activity
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;


    Marker allMarkers;
    FusedLocationProviderClient locationProvider;
    double currentLatitude = 0;
    double currentLongitude = 0;
    PlacesService placesService;

    PlacesResponse placesResponse;
    List<PlacesModel> getPlaceModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapSpinner = findViewById(R.id.mapSpinner);
        btMapSearch = findViewById(R.id.btnMapSearch);

        // Set the Spinner Adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.MapLocations));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(spinnerAdapter);

        /* https://stackoverflow.com/questions/20096177/error-inflating-class-fragment-in-mapfragment/20096245*/
        if (mMap == null){
            // Obtain the SupportMapFragment and get notified when map is ready to be used
            mapFragment = (SupportMapFragment) getSupportFragmentManager().
                    findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);

            if (mMap !=null){
                // The Map is verified.
            }
        } else {
            // if unsuccessful
            Toast.makeText(MapActivity.this, "Error In generating Map", Toast.LENGTH_SHORT).show();
        }


    } // end of onCreate

    // This is where we can ad markers or lines add listeners or move the camera.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    } // end of onMapReady

}

/*
        // Create an instance for retrofit API
        placesService = PlacesRetrofit.getPlacesRetrofit().create(PlacesService.class);
        getPlaceModelList = new ArrayList<>();

        // initialise fused client location provider i.e. currentLocation.getLatitude(), currentLocation.getLongitude()
        locationProvider = LocationServices.getFusedLocationProviderClient(this);

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // set the GoogleMap to Normal/Terrain

        assert mapFragment != null;
        mapFragment.getMapAsync(this);
 */
