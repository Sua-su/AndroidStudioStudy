package com.example.a1901153test.data.remote

import com.example.a1901153test.data.model.MovieDetailDto
import com.example.a1901153test.data.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    // 현재 상영중인 영화 목록
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ko-KR",
        @Query("page") page: Int = 1
    ): MovieListResponse

    // 인기 영화 목록
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ko-KR",
        @Query("page") page: Int = 1
    ): MovieListResponse

    // 영화 상세 정보
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ko-KR"
    ): MovieDetailDto
}
