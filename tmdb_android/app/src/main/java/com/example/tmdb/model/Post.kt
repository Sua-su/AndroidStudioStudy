package com.example.tmdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val authorNickname: String?, // Null means anonymous
    val createdAt: Long = System.currentTimeMillis()
)
