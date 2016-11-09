package com.example.nicholashall.myapplication.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nicholashall on 11/7/16.
 */

public class User {

    @SerializedName("Longitude")
    private double Longitude;

    @SerializedName("Latitude")
    private  double Latitude;

    @SerializedName("FullName")
    private String fullName;

    @SerializedName("AvatarBase64")
    private String avatarBase64;

    @SerializedName("CaughtUserId")
    private String caughtUserId;

    @SerializedName("RadiusInMeters")
    private Integer radiusInMeters;

    @SerializedName("Email")
    private String email;

    public User() {
    }

    public User(String caughtUserId, Integer radiusInMeters) {
        this.caughtUserId = caughtUserId;
        this.radiusInMeters = radiusInMeters;
    }

    public User(String avatarBase64, String fullName) {
        this.avatarBase64 = avatarBase64;
        this.fullName = fullName;
    }

    public User(double longitude, double latitude) {
        Longitude = longitude;
        Latitude = latitude;
    }




    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatarBase64() {
        return avatarBase64;
    }

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }

    public String getCaughtUserId() {
        return caughtUserId;
    }

    public void setCaughtUserId(String caughtUserId) {
        this.caughtUserId = caughtUserId;
    }

    public Integer getRadiusInMeters() {
        return radiusInMeters;
    }

    public void setRadiusInMeters(Integer radiusInMeters) {
        this.radiusInMeters = radiusInMeters;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
