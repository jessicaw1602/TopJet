package com.example.topjet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.topjet.PlacesApi.PermissionUtils;
import com.example.topjet.PlacesApi.PlacesMap;
import com.example.topjet.PlacesApi.PlacesResult;
import com.example.topjet.PlacesApi.RetrofitService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private List<PlacesResult> placesResultsList;

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

        // Build the map
        mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        placesResultsList = new ArrayList<>();

        // if the user hasn't set any permissions yet.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            Toast.makeText(this, "Error! Cannot get location!", Toast.LENGTH_SHORT).show();
            return;
        }
        checkGooglePlayServices();
        //TODO - replaces current location from the Retrofit Request
        // & replace the String 'type' and get user request from the button
        enableMyLocation(); // enable user's location
        getCurrentLocation(); // get their current location and pass it into getPlaces data
        getPlacesData(); // return all the nearbyPlaces from the API Call

    } // end of onCreate



    // This is where we can add markers or lines add listeners or move the camera.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // set the map to normal

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

    } // end of onMapReady

    // If you press on Click Listener then do private void getPlacesData(String type){}
    // Code Adapted From: https://www.codeproject.com/Articles/1121069/Google-Maps-Nearby-Places-API-using-Retrofit-Andro
    private void getPlacesData(){
        String type = "art_gallery"; // you will have the opportunity to change this to museums
        String keyword = "aboriginal";
        String location = "-33.8670522,151.1957362"; // OR you can do it like this: latitude + "," + longitude
        String key = "AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y";
        float radius = 1600;

        // get the base url from
        String BASE_URL = RetrofitService.BASE_URL;
        Log.d(TAG, "BASE_URL = " + BASE_URL);

        // Build the Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call <PlacesMap> call = service.getNearbyPlaces(type, keyword, location, radius, key);

        call.enqueue(new Callback<PlacesMap>() {
            @Override
            public void onResponse(@NonNull Call<PlacesMap> call, @NonNull Response<PlacesMap> response) {
                if (response.isSuccessful() && response.errorBody() == null){
                    if (response.body().getResults() != null && response.body().getResults().size() > 0){
                        placesResultsList.clear();
                        mMap.clear();

                        // Return the values now
                        for (int i = 0; i < response.body().getResults().size(); i++){
                            placesResultsList.add(response.body().getResults().get(i));

                            double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                            double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                            String placeName = response.body().getResults().get(i).getName();

                            MarkerOptions markerOptions = new MarkerOptions();
                            LatLng latLng = new LatLng(lat, lng);

                            // adding the marker options
                            markerOptions.position(latLng);
                            markerOptions.title(placeName);
                            Marker m = mMap.addMarker(markerOptions); // adding marker to camera

                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            // move map camera
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                            Log.d(TAG, "DATA RETRIEVED SUCCESSFULLY");
                        }
                    }

                } else {
                    Log.d(TAG, "ERROR WITHIN RESPONSE: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PlacesMap> call, Throwable t) {
                Log.d(TAG, "Failure" + t.toString());
            }
        }); // end of enqueue

    } // end of getPlacesData





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
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLatLong, 10));
//                                mMap.addMarker(markerOptions);
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

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
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

