package com.pushe.worker.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pushe.worker.BuildConfig
import com.pushe.worker.logup.dataStore
import com.pushe.worker.logup.userId
import com.pushe.worker.settings.data.AccountRepository
import com.pushe.worker.utils.ERPRestHelper
import com.pushe.worker.utils.ERPRestHelperImpl
import com.pushe.worker.utils.ERPRestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideUserId(): String = userId

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideOkHttpClient(
        repository: AccountRepository
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
        }
        builder.addInterceptor {
            val credentials = Credentials.basic(
                repository.account,
                repository.password,
                StandardCharsets.UTF_8
            )
            var request = it.request()
            request = request.newBuilder().header("Authorization", credentials).build()
            return@addInterceptor it.proceed(request)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        repository: AccountRepository,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(repository.path)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideERPRestService(
        retrofit: Retrofit
    ): ERPRestService = retrofit.create(ERPRestService::class.java)

    @Provides
    @Singleton
    fun provideERPRestHelper(
        helper: ERPRestHelperImpl
    ): ERPRestHelper = helper

}