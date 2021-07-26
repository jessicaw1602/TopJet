package com.example.topjet.PlacesApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* Code adapted from https://www.youtube.com/watch?v=5QkB1-ln8H0&t=141s */
public class PlacesRetrofit {

    private static Retrofit retrofit;
    public static String BASE_URL = "https://maps.googleapis.com";

    public static Retrofit getPlacesRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    //.addConverterFactor(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;

    } // end of getPlacesRetrofit method

}
