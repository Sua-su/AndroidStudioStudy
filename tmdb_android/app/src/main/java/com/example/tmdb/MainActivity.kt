package com.example.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.tmdb.ui.board.BoardScreen
import com.example.tmdb.ui.board.PostAddScreen
import com.example.tmdb.ui.detail.DetailScreen
import com.example.tmdb.ui.home.HomeScreen
import com.example.tmdb.ui.profile.ProfileScreen
import com.example.tmdb.ui.profile.ProfileViewModel
import com.example.tmdb.ui.reviews.ReviewsScreen
import com.example.tmdb.ui.search.SearchScreen
import com.example.tmdb.ui.trend.TrendScreen
import com.example.tmdb.ui.theme.TmdbTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TmdbTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TmdbApp()
                }
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Trend : Screen("trend", "Trend", Icons.Default.Star)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Board : Screen("board", "Board", Icons.Default.DateRange)
    object Reviews : Screen("reviews", "Reviews", Icons.Default.List)
    object Profile : Screen("profile", "Profile", Icons.Default.AccountCircle)
}

@Composable
fun TmdbApp(profileViewModel: ProfileViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Trend, Screen.Search, Screen.Board, Screen.Reviews, Screen.Profile)
    val nickname by profileViewModel.nickname.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(onMovieClick = { movieId ->
                    navController.navigate("detail/$movieId")
                })
            }
            composable(Screen.Trend.route) {
                TrendScreen()
            }
            composable(Screen.Search.route) {
                SearchScreen(onMovieClick = { movieId ->
                    navController.navigate("detail/$movieId")
                })
            }
            composable(Screen.Board.route) {
                BoardScreen(onAddPostClick = {
                    navController.navigate("post_add")
                })
            }
            composable("post_add") {
                PostAddScreen(
                    onBackClick = { navController.popBackStack() },
                    authorNickname = nickname
                )
            }
            composable(Screen.Reviews.route) {
                ReviewsScreen(onReviewClick = { movieId ->
                    navController.navigate("detail/$movieId")
                })
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel = profileViewModel)
            }
            composable(
                route = "detail/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) {
                DetailScreen(onBackClick = {
                    navController.popBackStack()
                })
            }
        }
    }
}
