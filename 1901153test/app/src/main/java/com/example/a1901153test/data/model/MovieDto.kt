package com.example.a1901153test.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TMDB API 응답 - 영화 목록용
@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList()
)

// 목록 API 응답 wrapper
@Serializable
data class MovieListResponse(
    val page: Int,
    val results: List<MovieDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

// TMDB API 응답 - 영화 상세용
@Serializable
data class MovieDetailDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    val runtime: Int? = null,
    val genres: List<GenreDto> = emptyList(),
    val status: String = "",
    val tagline: String = ""
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)
