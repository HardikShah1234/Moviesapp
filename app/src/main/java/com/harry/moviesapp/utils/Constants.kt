package com.harry.moviesapp.utils

class Constants {
    companion object {
        const val BASE_URL = "https://movies-sample.herokuapp.com"
        const val CACHE_NAME = "HttpCache"
        const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB
        const val CACHE_CONTROL_HEADER = "Cache-Control"
        const val MAX_CACHE_AGE = 10 // 10 minutes
    }
}