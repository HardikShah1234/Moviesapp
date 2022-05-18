package com.harry.moviesapp.network

import com.harry.moviesapp.utils.Constants.Companion.CACHE_CONTROL_HEADER
import com.harry.moviesapp.utils.Constants.Companion.MAX_CACHE_AGE
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheIntercepter : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
            .newBuilder()
            .header(
                CACHE_CONTROL_HEADER,
                CacheControl.Builder().maxAge(MAX_CACHE_AGE, TimeUnit.MINUTES).build().toString()
            ).build()
    }
}