
package com.example.postsv2.data.repository
import com.example.postsv2.data.dto.PostDto
import com.example.postsv2.data.remote.PostApi
import javax.inject.Inject
class PostRepositoryRemoteImpl @Inject constructor(
    private val postApi: PostApi
) : PostRepository {
    override suspend fun getPosts(): List<PostDto> = postApi.getPosts()
    override suspend fun getPost(id: Int): PostDto = postApi.getPost(id)
}