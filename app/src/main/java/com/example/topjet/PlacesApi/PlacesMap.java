package com.example.topjet.PlacesApi;

import com.google.android.gms.common.api.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlacesMap {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = new ArrayList<Object>();

    @SerializedName("results")
    @Expose
    private List<PlacesResult> results = new ArrayList<PlacesResult>();

    @SerializedName("status")
    @Expose
    private String status;

    // Setters
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }
    public void setResults(List<PlacesResult> results) {
        this.results = results;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // Getters
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public List<PlacesResult> getResults() {
        return results;
    }
    public String getStatus() {
        return status;
    }

}
