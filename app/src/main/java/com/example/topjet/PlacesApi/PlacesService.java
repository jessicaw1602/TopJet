package com.example.topjet.PlacesApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

// Retrofit API
public interface PlacesService {
    @GET // return all the necessary information based on the Places
    Call<PlacesResponse> getNearByPlaces (@Url String url);

    /*
    @GET("positions.json?description=sql&location=sydney")
    Call<ArrayList<GitHubJobs>> getGitHubJob();
     */
}
