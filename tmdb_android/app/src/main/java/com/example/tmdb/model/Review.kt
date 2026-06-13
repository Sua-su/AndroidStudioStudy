package com.example.tmdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val movieId: Int,
    val movieTitle: String,
    val rating: Float,
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)
