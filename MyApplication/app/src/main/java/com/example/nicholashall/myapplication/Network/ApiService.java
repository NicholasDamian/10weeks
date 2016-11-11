package com.example.nicholashall.myapplication.Network;


import com.example.nicholashall.myapplication.Models.Account;
import com.example.nicholashall.myapplication.Models.Authorization;
import com.example.nicholashall.myapplication.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Nicholas.Hall on 10/31/16.
 */

public interface ApiService {

    @POST("/api/Account/Register")
    Call<Void> register(@Body Account account);

    @FormUrlEncoded
    @POST("token")
    Call<Authorization> login(@Field("username") String email, @Field("password") String password, @Field("grant_type") String grantType);

    @POST("/v1/User/CheckIn")
    Call<Void> CheckIn(@Body User user);

    @GET("/v1/User/Nearby")
    Call<User[]> getUsers(@Query("RadiusInMeters") Integer radius);

    @GET("/api/Account/UserInfo")
    Call<Account>getUserInfo();

    @POST("/api/Account/UserInfo")
    Call<Void>postUserInfo(@Body Account account);

    @GET ("/v1/User/Caught")
    Call<User[]> caughtUsers();

    @POST("/v1/User/Catch")
    Call<Void> catchUser(@Body User user);

}
