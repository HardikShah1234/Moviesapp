package com.harry.moviesapp.repository

import com.harry.moviesapp.data.Data
import com.harry.moviesapp.network.ApiService
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getMoviesData(): List<Data> = apiService.getMoviesResponse().data
}