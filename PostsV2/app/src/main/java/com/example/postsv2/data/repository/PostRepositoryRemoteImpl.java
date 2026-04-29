package com.example.postsv2.data.repository;
import com.example.postsv2.data.dto.PostDto;
import com.example.postsv2.data.remote.PostApi;
import com.example.postsv2.data.remote.RetrofitInstance;
class PostRepositoryRemoteImpl(
        private val postApi: PostApi = RetrofitInstance.postApi
) : PostRepository {
    override suspend fun getPosts(): List<PostDto> = postApi.getPosts()
    override suspend fun getPost(id: Int): PostDto = postApi.getPost(id)
}