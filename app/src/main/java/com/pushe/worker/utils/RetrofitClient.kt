package com.pushe.worker.utils

import com.pushe.worker.settings.data.AccountPreferences
import com.pushe.worker.settings.data.AccountRepository
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets.*

object RetrofitClient {

    private var retrofit: Retrofit? = null
    private var oldPreferences: AccountPreferences? = null

    fun getClient(): Retrofit {
        val preferences = AccountRepository.getPreferences().value
        if (!(retrofit != null && preferences == oldPreferences)) {
            val loggingInterceptor = HttpLoggingInterceptor() //TODO: Delete post debug
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client: OkHttpClient = OkHttpClient().newBuilder()
                .addInterceptor {
                    val credentials = Credentials.basic(
                        preferences.account,
                        preferences.password,
                        UTF_8)
                    var request = it.request()
                    request = request.newBuilder().header("Authorization", credentials).build()
                    return@addInterceptor it.proceed(request)
                }
                .addInterceptor { loggingInterceptor.intercept(it) }
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(preferences.path)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            oldPreferences = preferences
        }
        return retrofit!!
    }
}