package com.softmiracle.githubmvp.data.api;

import com.softmiracle.githubmvp.data.models.GHRepo;
import com.softmiracle.githubmvp.data.models.GHUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Denys on 25.02.2017.
 */

public interface GHApi {

    @GET("/users/{user}/repos")
    Call<List<GHRepo>> getRepo(@Path("user") String user);

    @GET("/users/{user}")
    Call<GHUser> getUserProfile(@Path("user") String user);
}
