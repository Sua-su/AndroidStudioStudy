package com.example.tmdb.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val results: List<MovieDto>
)

data class MovieDto(
    val id: Int,
    val title: String?,
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Float?
)

fun MovieDto.toDomain() = Movie(
    id = id,
    title = title ?: "제목 없음",
    overview = overview ?: "상세 줄거리가 제공되지 않습니다.",
    posterUrl = if (posterPath != null) "https://image.tmdb.org/t/p/w500$posterPath" else null,
    releaseDate = releaseDate ?: "미상",
    rating = voteAverage ?: 0f
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: String,
    val rating: Float
)
