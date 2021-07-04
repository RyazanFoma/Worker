package com.pushe.worker.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ERPRestService {
    private val BASE_URL: String = "http://10.0.2.2:51515/testhttp/hs/getSomething/"

//    val preference = PreferenceAccount(context)
//    val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain: Interceptor.Chain ->
//        val originalRequest = chain.request()
//        val builder = originalRequest.newBuilder()
//                .header("Authorization",
//                        Credentials.basic(preference.account, preference.password))
//        val newRequest = builder.build()
//        chain.proceed(newRequest)
//    }.build()
//
//    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
//        this.level = HttpLoggingInterceptor.Level.BODY
//    }

    private val okHttpClient : OkHttpClient = OkHttpClient.Builder().apply {
        //  this.addInterceptor(interceptor)
    }.build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val restClient = retrofit.create(RestApiService::class.java)
}