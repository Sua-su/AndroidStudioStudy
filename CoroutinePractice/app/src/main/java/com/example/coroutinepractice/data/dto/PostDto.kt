package com.example.coroutinepractice.data.dto

import android.R
import kotlinx.serialization.Serializable
import retrofit2.http.Body


@Serializable
data class PostDto (
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)