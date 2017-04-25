package com.softmiracle.githubmvp.data.interactor.impl;

import com.softmiracle.githubmvp.data.interactor.InteractorCallback;
import com.softmiracle.githubmvp.data.interactor.UserStarredInteractor;
import com.softmiracle.githubmvp.data.models.Repo;
import com.softmiracle.githubmvp.data.service.GitHubService;
import com.softmiracle.githubmvp.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dnsfrolov on 25.04.2017.
 */

public class UserStarredInteractorImpl implements UserStarredInteractor {

    private GitHubService mApi;

    public UserStarredInteractorImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApi = retrofit.create(GitHubService.class);
    }

    @Override
    public void getUserStarred(String user, int page, final InteractorCallback<List<Repo>> callback) {
        mApi.getUserStarred(user, page).enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
