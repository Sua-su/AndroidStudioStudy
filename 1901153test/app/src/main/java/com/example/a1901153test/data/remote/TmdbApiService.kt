package com.example.a1901153test.data.remote

import com.example.a1901153test.data.model.*
import retrofit2.http.*

interface TmdbApiService {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("api_key") apiKey: String, @Query("language") lang: String = "ko-KR"): MovieListResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String, @Query("language") lang: String = "ko-KR"): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") id: Int, @Query("api_key") apiKey: String, @Query("language") lang: String = "ko-KR"): MovieDetailDto
}
