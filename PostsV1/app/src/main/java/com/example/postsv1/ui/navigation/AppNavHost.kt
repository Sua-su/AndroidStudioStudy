package com.example.postsv1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.postsv1.ui.detail.PostDetailScreen
import com.example.postsv1.ui.list.PostListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PostListRoute
    ) {
        composable<PostListRoute> {
            PostListScreen(
                onPostClick = { postId ->
                    navController.navigate(PostDetailRoute(postId = postId))
                }
            )
        }
        composable<PostDetailRoute> { backStackEntry ->
            val route: PostDetailRoute = backStackEntry.toRoute()
            PostDetailScreen(
                postId = route.postId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
