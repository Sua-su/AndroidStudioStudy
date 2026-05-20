package com.example.a1901153test.data

import com.example.a1901153test.data.remote.TmdbApiService
import javax.inject.*

@Singleton
class MovieRepository @Inject constructor(private val api: TmdbApiService) {
    private val key = "c12ed457b94399d3c810d10b94e4e4c5"

    suspend fun getNowPlayingMovies() = api.getNowPlayingMovies(key).results
    suspend fun getPopularMovies() = api.getPopularMovies(key).results
    suspend fun getMovieDetail(id: Int) = api.getMovieDetail(id, key)
}
