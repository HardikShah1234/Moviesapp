package com.harry.moviesapp.network

import com.harry.moviesapp.data.MoviesResponse
import retrofit2.http.GET

interface ApiService {

    @GET("/api/movies")
    suspend fun getMoviesResponse(): MoviesResponse
}