package com.example.a1901153test.data

import com.example.a1901153test.data.model.MovieDetailDto
import com.example.a1901153test.data.model.MovieDto
import com.example.a1901153test.data.remote.TmdbApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val apiService: TmdbApiService
) {
    // API 키 (초보자 스타일 - 실제로는 절대 이렇게 하면 안 됨!)
    private val apiKey = "c12ed457b94399d3c810d10b94e4e4c5"

    suspend fun getNowPlayingMovies(): List<MovieDto> {
        return apiService.getNowPlayingMovies(apiKey = apiKey).results
    }

    suspend fun getPopularMovies(): List<MovieDto> {
        return apiService.getPopularMovies(apiKey = apiKey).results
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetailDto {
        return apiService.getMovieDetail(movieId = movieId, apiKey = apiKey)
    }
}
