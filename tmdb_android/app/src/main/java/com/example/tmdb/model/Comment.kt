package com.example.tmdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val authorNickname: String?,
    val authorId: Long? = null,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)
