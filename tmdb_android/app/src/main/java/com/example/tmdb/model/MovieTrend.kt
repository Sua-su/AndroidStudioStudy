package com.example.tmdb.model

data class MovieTrend(
    val title: String,
    val rank: String,
    val image: String?,
    val link: String? = null
)
