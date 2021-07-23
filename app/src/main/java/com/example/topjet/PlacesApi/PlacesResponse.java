package com.example.topjet.PlacesApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// Use Retrofit to make the call
/* Source code adapted from: https://www.youtube.com/watch?v=5QkB1-ln8H0&t=141s*/

// Returns the list of PlacesModel that we sent to via the API Request
public class PlacesResponse {

    @SerializedName("results")
    @Expose
    private List<PlacesModel> mapPlaceList;

    // Setter
    public void setMapPlaceList(List<PlacesModel> mapPlaceList){
        this.mapPlaceList = mapPlaceList;
    }

    // Getter
    public List<PlacesModel> getMapPlaceList(){
        return mapPlaceList;
    }


}
