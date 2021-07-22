package com.example.topjet.PlacesApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Returns the LocationModel

public class GeometryModel {

    @SerializedName("location")
    @Expose
    private LocationModel location;

    // Setters
    public LocationModel getLocation() {
        return location;
    }

    // Getters
    public void setLocation(LocationModel location) {
        this.location = location;
    }

}
