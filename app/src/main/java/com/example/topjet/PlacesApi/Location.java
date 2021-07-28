package com.example.topjet.PlacesApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("lat")
    @Expose
    private float lat;

    @SerializedName("lng")
    @Expose
    private float lng;

    // Setters
    public void setLat(float lat) {
        this.lat = lat;
    }
    public void setLng(float lng) {
        this.lng = lng;
    }

    // Getters
    public float getLat() {
        return lat;
    }
    public float getLng() {
        return lng;
    }

}
