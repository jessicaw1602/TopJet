package com.example.topjet.PlacesApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/* https://developers.google.com/maps/documentation/places/web-service/search
 * Available Fields: business_status, formatted_address, geometry, icon, icon_mask_base_uri, icon_background_color, name, permanently_closed (deprecated), photos, place_id, plus_code, types
 * Used jsonschema2pojo to get the Java Objects: https://www.jsonschema2pojo.org/
 */

public class PlacesResult {
    @SerializedName("business_status")
    @Expose
    private String businessStatus;

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("icon_background_color")
    @Expose
    private String iconBackgroundColor;

    @SerializedName("icon_mask_base_uri")
    @Expose
    private String iconMaskBaseUri;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("photos")
    @Expose
    private List<Photo> photos = new ArrayList<Photo>();

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("plus_code")
    @Expose
    private PlusCode plusCode;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("reference")
    @Expose
    private String reference;

    @SerializedName("scope")
    @Expose
    private String scope;

    @SerializedName("types")
    @Expose
    private List<String> types = new ArrayList<String>();

    @SerializedName("user_ratings_total")
    @Expose
    private float userRatingsTotal;

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;

    // Getters & Setters
    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    public void setIconBackgroundColor(String iconBackgroundColor) {
        this.iconBackgroundColor = iconBackgroundColor;
    }

    public String getIconMaskBaseUri() {
        return iconMaskBaseUri;
    }

    public void setIconMaskBaseUri(String iconMaskBaseUri) {
        this.iconMaskBaseUri = iconMaskBaseUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public PlusCode getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(PlusCode plusCode) {
        this.plusCode = plusCode;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public float getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(float userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }
}
