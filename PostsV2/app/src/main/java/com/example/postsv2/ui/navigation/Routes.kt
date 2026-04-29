package com.example.postsv2.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object PostListRoute

@Serializable
data class PostDetailRoute(val postId: Int)
