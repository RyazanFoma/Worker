package com.pushe.worker.data;

import android.content.Context;

import com.pushe.worker.data.model.LoggedInUser;
import com.pushe.worker.utils.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends MutableLiveData<Result<?>> {

    private final Context context;

    public LoginDataSource(Context context) {
        this.context = context;
    }

    public void requestUser(String barCode) {
        try {
            Retrofit retrofit = RetrofitClient.INSTANCE.getClient(context);
            Call<LoggedInUser> call = retrofit.create(ERPRestService.class).getUser1(barCode);
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