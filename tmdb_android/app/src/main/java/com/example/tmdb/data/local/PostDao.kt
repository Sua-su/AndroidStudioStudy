package com.example.tmdb.data.local

import androidx.room.*
import com.example.tmdb.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Long): Post?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)
}
