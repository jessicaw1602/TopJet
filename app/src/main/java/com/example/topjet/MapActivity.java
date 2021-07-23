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
https://developers.google.com/maps/documentation/places/web-service/search?hl=en_GB */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapActivity";

    Spinner mapSpinner;
    Button btMapSearch;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
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
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        // Create an instance for retrofit API
        placesService = PlacesRetrofit.getPlacesRetrofit().create(PlacesService.class);
        getPlaceModelList = new ArrayList<>();

        // Set the Spinner Adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, getResources().getStringArray(R.array.MapLocations));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(spinnerAdapter);

        // initialise fused client location provider i.e. currentLocation.getLatitude(), currentLocation.getLongitude()
        locationProvider = LocationServices.getFusedLocationProviderClient(this);

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // set the GoogleMap to Normal/Terrain

        assert mapFragment != null;
        //TODO - i need to inflate the fragment first...
        mapFragment.getMapAsync(this);


    } // end of onCreate

    private void getPlaces(String place) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=art_gallery&keyword=aboriginal&key=AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y";
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // replace the location with the current location.
            placesService.getNearByPlaces(url).enqueue(new Callback<PlacesResponse>() {
                @Override
                public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                    if (response.isSuccessful()) {
                        getPlaceModelList.clear(); // clear the array
                        mMap.clear(); // clear Google Maps

                        for (int i = 0; i < response.body().getMapPlaceList().size(); i++) {
                            getPlaceModelList.add(response.body().getMapPlaceList().get(i));
                            allMarkers(response.body().getMapPlaceList().get(i), i);

                        } // end of for Statement

                    } else {
                        Log.d(TAG, "Error" + response.errorBody());
                        Toast.makeText(MapActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    } // end of if-else statement

                }

                @Override
                public void onFailure(Call<PlacesResponse> call, Throwable t) {
                    Log.d(TAG, "Failure");
                }
            }); // end of enqueue

        }
        // TODO - check what isLocationPermissionOk
//        if (isLocationPermissionOk){} // end of if-else statement

    } // end of getPlaces method

    /* Code Adapted from: https://stackoverflow.com/questions/18053156/set-image-from-drawable-as-marker-in-google-map-version-2 */
    private void allMarkers(PlacesModel placesModel, int position) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(placesModel.getGeometry().getLocation().getLat(),
                        placesModel.getGeometry().getLocation().getLng()))
                .title(placesModel.getName())
                .snippet(placesModel.getVicinity());
        markerOptions.icon(getIcon());
        mMap.addMarker(markerOptions).setTag(position);


    } // end of allMarkers method

    /* Code Adapted from: https://stackoverflow.com/questions/18053156/set-image-from-drawable-as-marker-in-google-map-version-2 */
    private BitmapDescriptor getIcon() {
        Drawable drawable = getResources().getDrawable(R.drawable.profile_location);
//        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(drawable);

        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getGeoLocation();
            }
        }
    } // end of onRequestPermissionsResult

    //TODO
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap = mMap;
        setUpGoogleMap();
    }

    private void setUpGoogleMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this::onMarkerClick);
    } // end of setUpGoogleMap method



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }


}

/*
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

//            }
//                    }); // end of btMapSearch.setOnClickListener

// */