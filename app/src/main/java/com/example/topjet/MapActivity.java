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
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.example.topjet.PlacesApi.PermissionUtils;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;

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

/* Code adapted from:
 * https://developers.google.com/maps/documentation/places/web-service/search?hl=en_GB &&
 * https://developers.google.com/maps/documentation/android-sdk/start
 * To get 'My Location', code adapted from: https://github.com/googlemaps/android-samples/blob/master/ApiDemos/java/app/src/gms/java/com/example/mapdemo/MyLocationDemoActivity.java
 * Select Current Place: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MapActivity";
    //    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=art_gallery&keyword=aboriginal&key=AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y";

    Spinner mapSpinner;
    Button btMapSearch;

    // Main elements for the maps activity
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    // The entry point to the Places API.
    private PlacesClient placesClient;
    FusedLocationProviderClient client; // entry point to fused location
    private Location lastKnownLocation; // the last known location retrieved by the Fused Location Provider

    // Get user's location
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private boolean locationPermissionGranted;
    private static final int DEFAULT_ZOOM = 16;
    Marker allMarkers;

    // For Retrofit
    PlacesService placesService;
    List<PlacesModel> getPlaceModelList;
    PlacesResponse placesResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapSpinner = findViewById(R.id.mapSpinner);
        btMapSearch = findViewById(R.id.btnMapSearch);

        // Create an instance for retrofit API
        placesService = PlacesRetrofit.getPlacesRetrofit().create(PlacesService.class);
        getPlaceModelList = new ArrayList<>();

        // Set the Spinner Adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.MapLocations));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(spinnerAdapter);

        // Build the map
        mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(this);

        // if the user hasn't set any permissions yet.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            Toast.makeText(this, "Error! Cannot get location!", Toast.LENGTH_SHORT).show();
            return;
        }
        enableMyLocation();
        getCurrentLocation();

    } // end of onCreate

    // This is where we can ad markers or lines add listeners or move the camera.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // set the map to normal
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

    } // end of onMapReady

    // Now call RETROFIT
    private void getPlaces(String placeName){

        //TODO - replace the location with the current location & replace the type with the adapter
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=-33.8688197,151.2092955"
                + "&radius=1800"
                + "&type=art_gallery"
                + "&keyword=aboriginal"
                + "&key=AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y";

        // would need to do an if-else statement; you need to check if the current location is null
        // or not
        /*  if (currentLocation != null){} */

        placesService.getNearByPlaces(url).enqueue(new Callback<PlacesResponse>() {
            @Override
            public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                // check to see whether the call was successful or not
                if (response.errorBody() == null && response.isSuccessful()){ // if the call is successful
                    // Clear all the map and the arraylist (that will store the vales) first
                    getPlaceModelList.clear();
                    mMap.clear();

                    // retrieve all the values first
                    for (int i = 0; i < response.body().getMapPlaceList().size(); i++){
                        // we then want to store all the responses (attributes) into an arrayList
                        getPlaceModelList.add(response.body().getMapPlaceList().get(i));
                        Log.d(TAG, response.toString());
                        // now we want to add markers to the position of
                        addMarker(response.body().getMapPlaceList().get(i));
                    } // end of for method

                } else { // if there is an error in retrieving the values
                    Log.d(TAG, "unable to retrieve the values" + response.errorBody());
                    Toast.makeText(getApplicationContext(), "Error: " + response.errorBody(),
                            Toast.LENGTH_SHORT).show();
                } // end of if-else
            }

            @Override
            public void onFailure(Call<PlacesResponse> call, Throwable t) {
                Log.d(TAG, "Failed to connect to API");
            }
        }); // end of enqueue

    } // end of getPlaces method

    // Now we want to be able to add in the markers for all the places
    private void addMarker(PlacesModel placesModel) {
    }

    /** DON'T TOUCH THE BOTTOM OF THE CODE **/
    private void getCurrentLocation() {
        // Check permissions
        if (ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // If permission is granted, get the user's location
            Task<Location> taskLocation = client.getLastLocation();
            taskLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location !=null){
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                // Get the long & lat of the location
                                LatLng getLatLong = new LatLng(location.getLatitude(), location.getLongitude());

                                // Create Marker options
                                MarkerOptions markerOptions = new MarkerOptions().position(getLatLong)
                                        .title("Your Location");

                                // move the camera and zoom in closer
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLong, 16));
                                mMap.addMarker(markerOptions);
                            }
                        });
                    }
                }
            }); // end of addOnSuccessListener

        } else { // request permissions
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);

            Toast.makeText(this, "Error! Cannot get location!", Toast.LENGTH_SHORT).show();
        }

    } // end of getCurrentLocation method

    // Enables the user's location, if the 'Fine' location permission has been granted
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    } // end of enableMyLocation()

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false; // the camera will animate to the user's current position
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

}

/*
    // This is where we can ad markers or lines add listeners or move the camera.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // set the map to normal
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

        // add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        // move the camera and zoom in closer
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        // Centering the map within an area
        /* https://developers.google.com/maps/documentation/android-sdk/views */
//LatLngBounds australiaBounds = new LatLngBounds(
//        new LatLng(-44, 113), // SW bounds
//        new LatLng(-10, 154)  // NE bounds
//);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(australiaBounds.getCenter(), 10));
    // mMap.setLatLngBoundsForCameraTarget(australiaBounds); // constraining the camera target to specific areas
//} // end of onMapReady
// */

