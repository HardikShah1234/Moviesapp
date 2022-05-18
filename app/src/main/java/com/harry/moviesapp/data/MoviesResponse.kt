package com.harry.moviesapp.data


import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("data")
    val `data`: List<Data>
)