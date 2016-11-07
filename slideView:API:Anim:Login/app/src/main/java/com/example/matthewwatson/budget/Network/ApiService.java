package com.example.matthewwatson.budget.Network;

import com.example.matthewwatson.budget.Model.TestPost;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Matthew.Watson on 10/31/16.
 */

public interface ApiService {
    @GET("/posts/{id}") //GET call per documentation
    Call<TestPost> getPost(@Path("id") Integer id); //call getPost to return ID
    //query via URL

    @POST("/posts") //Body requests
    Call<TestPost> postPost(@Body TestPost post);

    @GET("/posts")
    Call<TestPost[]> getPosts();


}
