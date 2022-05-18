package com.harry.moviesapp.di

import android.content.Context
import com.harry.moviesapp.network.ApiService
import com.harry.moviesapp.network.CacheIntercepter
import com.harry.moviesapp.utils.Constants.Companion.BASE_URL
import com.harry.moviesapp.utils.Constants.Companion.CACHE_NAME
import com.harry.moviesapp.utils.Constants.Companion.CACHE_SIZE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(File(context.cacheDir.absolutePath, CACHE_NAME), CACHE_SIZE)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(CacheIntercepter())
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(gsonConverterFactory)
            .client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}