package com.example.topjet.PlacesApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// get API response in JSON and convert JSON  using jsonschema2pojo & getting the Result

/*
GeometryModel -> returns the location
icon -> returns the icon of place
name -> name of the place
placeId -> id given by Google
types -> the place types given by Google. API request will get art_gallery
 */

public class PlacesModel {

    @SerializedName("business_status")
    @Expose
    private String businessStatus;

    @SerializedName("geometry")
    @Expose
    private GeometryModel geometry;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("name")
    @Expose
    private String name;

//    @SerializedName("photos")
//    @Expose
//    private List<PhotoModel> photos = null;

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("rating")
    @Expose
    private Integer rating;

    @SerializedName("reference")
    @Expose
    private String reference;

    @SerializedName("scope")
    @Expose
    private String scope;

    @SerializedName("types")
    @Expose
    private List<String> types = null;

    @SerializedName("user_ratings_total")
    @Expose
    private Integer userRatingsTotal;

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    @SerializedName("opening_hours")
    @Expose
    private HoursModel openingHours;

    // Setters
    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public void setGeometry(GeometryModel geometry) {
        this.geometry = geometry;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setPhotos(List<PhotoModel> photos) {
//        this.photos = photos;
//    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setOpeningHours(HoursModel openingHours) {
        this.openingHours = openingHours;
    }

    // Getters
    public String getBusinessStatus() {
        return businessStatus;
    }

    public GeometryModel getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

//    public List<PhotoModel> getPhotos() {
//        return photos;
//    }

    public String getPlaceId() {
        return placeId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getReference() {
        return reference;
    }

    public String getScope() {
        return scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public String getVicinity() {
        return vicinity;
    }

    public HoursModel getOpeningHours() {
        return openingHours;
    }
}
