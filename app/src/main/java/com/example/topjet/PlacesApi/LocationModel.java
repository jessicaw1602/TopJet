package com.example.topjet.PlacesApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Return the latitude and longitude of each place
public class LocationModel {

    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lng")
    @Expose
    private Double lng;

    // Setters
    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    // Getters
    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

}
