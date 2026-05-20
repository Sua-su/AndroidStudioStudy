package com.example.a1901153test.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.a1901153test.ui.screen.MovieDetailScreen
import com.example.a1901153test.ui.screen.MovieListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "movie_list"
    ) {
        // 영화 목록 화면
        composable(route = "movie_list") {
            MovieListScreen(navController = navController)
        }

        // 영화 상세 화면 - movieId를 Int로 받음
        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) {
            MovieDetailScreen(navController = navController)
        }
    }
}
