package com.pushe.worker.data;

import android.content.Context;

import com.pushe.worker.data.model.LoggedInUser;
import com.pushe.worker.preference.PreferenceAccount;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

import androidx.lifecycle.MutableLiveData;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends MutableLiveData<Result<?>> {

    private final Context context;

    public LoginDataSource(Context context) {
        this.context = context;
    }

    public void requestUser(String id) {
        try {
            final PreferenceAccount preference = new PreferenceAccount(context);
            //Client to intercept authorization request
            final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder()
                        .header("Authorization",
                                Credentials.basic(preference.account, preference.password));
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }).build();
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(preference.path)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Call<LoggedInUser> call = retrofit.create(RestApiService.class).getUser(id);
            call.enqueue(new Callback<LoggedInUser>() {
                @Override
                public void onResponse(@NotNull Call<LoggedInUser> call,
                                       @NotNull Response<LoggedInUser> response) {
                    if (response.isSuccessful()) {
                        setValue(new Result.Success<>(response.body()));
                    }
                    else {
                        setValue(new Result.Error<>(new RuntimeException(
                                String.format(Locale.US, "%d - %s", response.code(), response.message()))));
                    }
                }

                @Override
                public void onFailure(@NotNull Call<LoggedInUser> call, @NotNull Throwable t) {
                    setValue(new Result.Error<>(new RuntimeException(t.getMessage())));
                }
            });
        } catch (Exception e) {
            setValue(new Result.Error<>(new IOException("Error logging in", e)));
        }
    }

}