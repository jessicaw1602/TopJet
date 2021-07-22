package com.example.topjet.PlacesApi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoModel {

    @SerializedName("height")
    @Expose
    private Integer height;

    @SerializedName("html_attributions")
    @Expose
    private List<String> htmlAttributions = null;

    @SerializedName("photo_reference")
    @Expose
    private String photoReference;

    @SerializedName("width")
    @Expose
    private Integer width;

    // Setters
    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    // Getters
    public Integer getHeight() {
        return height;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public Integer getWidth() {
        return width;
    }

}
