package com.example.coroutinepractice.data.remote

import com.example.coroutinepractice.data.dto.PostDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @GET("posts/{id}")
    suspend fun getPosts(@Path("id")id: Int): PostDto
}