package com.example.tmdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val id: String, // e.g., "first_review", "movie_buff_10"
    val name: String,
    val description: String,
    val iconResId: String, // String representation for now or a resource name
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)
