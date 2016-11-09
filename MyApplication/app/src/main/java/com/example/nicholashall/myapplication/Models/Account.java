package com.example.nicholashall.myapplication.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicholas.Hall on 11/7/16.
 */

public class Account {

    @SerializedName("FullName")
    private String fullName;

    @SerializedName("AvatarBase64")
    private String picture;

    @SerializedName("ApiKey")
    private String apiKey;

    @SerializedName("Password")
    private String password;

    @SerializedName("Email")
    private String email;


    public Account(String email, String fullName, String picture, String apiKey, String password) {
        this.email = email;
        this.fullName = fullName;
        this.picture = picture;
        this.apiKey = apiKey;
        this.password = password;
    }

    public Account(String picture, String fullName) {
        this.picture = picture;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
