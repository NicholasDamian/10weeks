package com.example.nicholashall.myapplication.Network;


import com.example.nicholashall.myapplication.Models.Account;
import com.example.nicholashall.myapplication.Models.Authorization;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Nicholas.Hall on 10/31/16.
 */

public interface ApiService {
//    @GET("/posts/{id}") //GET call per documentation
//    Call<TestPost> getPost(@Path("id") Integer id); //call getPost to return ID
//    //query via URL
//
//    @POST("/posts") //Body requests
//    Call<TestPost> postPost(@Body TestPost post);
//
//    @GET("/posts")
//    Call<TestPost[]> getPosts();

    @POST("/api/Account/Register")
    Call<Void> register(@Body Account account);



    @FormUrlEncoded
    @POST("token")
    Call<Authorization> login(@Field("username") String email, @Field("password") String password, @Field("grant_type") String grantType);




}
