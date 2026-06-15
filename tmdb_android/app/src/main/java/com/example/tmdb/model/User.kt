package com.example.tmdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val passwordHash: String, // In a real app, hash passwords! Storing plaintext here for local Room simplicity but naming it Hash to indicate intent.
    val nickname: String,
    val createdAt: Long = System.currentTimeMillis()
)
