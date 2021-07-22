package com.example.topjet.PlacesApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Return the hours of operation of the places
public class HoursModel {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    // Setter
    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    // Getter
    public Boolean getOpenNow() {
        return openNow;
    }


}
