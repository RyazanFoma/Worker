package com.pushe.worker.utils

import android.content.Context
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun getClient(context: Context): Retrofit {
        if (retrofit == null) {
            val preference = PreferenceAccount(context)
            val client: OkHttpClient = OkHttpClient().newBuilder().addInterceptor{
                it.proceed(it.request().newBuilder().
                addHeader("Authorization",
                    Credentials.basic(preference.account, preference.password)).build())}.build()
            retrofit = Retrofit.Builder()
                .baseUrl(preference.path)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}