package com.example.a1901153test.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.a1901153test.ui.screen.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "movie_list") {
        composable("movie_list") { MovieListScreen(navController) }
        composable("movie_detail/{movieId}", arguments = listOf(navArgument("movieId") { type = NavType.IntType })) {
            MovieDetailScreen(navController)
        }
    }
}
