package com.example.a1901153test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.a1901153test.navigation.NavGraph
import com.example.a1901153test.ui.theme._1901153testTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _1901153testTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
