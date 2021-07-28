package com.example.topjet.PlacesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    String BASE_URL = "https://maps.googleapis.com/maps/";

    //    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=art_gallery&keyword=aboriginal&key=AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y";

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
    // Example of Request: https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=YOUR_API_KEY
//    @GET("api/place/nearbysearch/json?key=AIzaSyCDho8QelBEkN-nkxAv8lCm1wnJ0bQl59Y")
    @GET("api/place/nearbysearch/json")
        Call<PlacesMap> getNearbyPlaces(@Query("type") String type, @Query("keyword") String keyword, @Query("location") String location, @Query("radius") float radius, @Query("key") String key);

    /*
        @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
     */
}
