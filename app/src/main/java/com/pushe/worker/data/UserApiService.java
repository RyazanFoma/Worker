package com.pushe.worker.data;

import com.pushe.worker.data.model.LoggedInUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * REST API to Retrofit to get a user account
 */

public interface UserApiService {
    @Headers("Accept: application/json")
    @GET("user")
    Call<LoggedInUser> getUser(@Query("id") String id);
}
