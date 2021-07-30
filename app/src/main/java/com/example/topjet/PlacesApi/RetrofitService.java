package com.example.topjet.PlacesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//  https://developers.google.com/maps/documentation/places/web-service/search
// @GET is used to call the server
// getNearbyPlaces method is used to get the details of the points
// PlacesMap is a POJO class used to store that response
// Required parameters: key, location and radius
// Optional parameters : keyword, type

// Required Parameters in our example below:
// type: =art_gallery
// input/keyword: aboriginal
// location:=-33.8670522,151.1957362
// radius:=1600
public interface RetrofitService {
    String BASE_URL = "https://maps.googleapis.com/maps/";

    // String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=art_gallery&keyword=aboriginal&key=AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y";

    @GET("api/place/nearbysearch/json")
        Call<PlacesMap> getNearbyPlaces(@Query("type") String type, @Query("keyword") String keyword, @Query("location") String location, @Query("radius") float radius, @Query("key") String key);

}
