package com.pushe.worker.utils

import android.content.Context
import com.pushe.worker.logup.ui.dataStore
import com.pushe.worker.settings.data.AccountPreferences
import com.pushe.worker.settings.data.AccountRepository
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null
    private var oldPreferences: AccountPreferences? = null

    fun getClient(context: Context): Retrofit {
        val preferences = AccountRepository.getPreferences(context.dataStore).value
        if (!(retrofit != null && preferences == oldPreferences)) {
            oldPreferences = preferences
            val client: OkHttpClient = OkHttpClient().newBuilder().addInterceptor{
                it.proceed(it.request().newBuilder().
                addHeader("Authorization",
                    Credentials.basic(preferences.account, preferences.password)).build())}.build()
            retrofit = Retrofit.Builder()
                .baseUrl(preferences.path)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}