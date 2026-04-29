package com.example.postsv2.data.repository;

import com.example.postsv2.data.dto.PostDto;
interface PostRepository {
    suspend fun getPosts(): List<PostDto>
    suspend fun getPost(id: Int): PostDto
}