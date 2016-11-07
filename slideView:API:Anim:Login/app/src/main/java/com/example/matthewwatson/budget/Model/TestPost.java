package com.example.matthewwatson.budget.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matthew.Watson on 10/31/16.
 */

public class TestPost {
    @SerializedName("userId")

    Integer userID;

    @SerializedName("id")
    Integer id;

    @SerializedName("title")
    String title;

    @SerializedName("body")
    String body;

    public TestPost(Integer userID, String title, String body) {
        this.userID = userID;
        this.title = title;
        this.body = body;
    }
    public TestPost() {

    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }





}
