package com.example.tmdb.ui.trend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tmdb.model.MovieTrend
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendScreen(
    viewModel: TrendViewModel = hiltViewModel()
) {
    val trends by viewModel.trends.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Movie Trends (KOBIS)") },
                actions = {
                    IconButton(onClick = { viewModel.fetchTrends() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (trends.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Failed to load trends.")
                    Button(onClick = { viewModel.fetchTrends() }) {
                        Text("Retry")
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
...

                items(trends) { trend ->
                    TrendItem(trend)
                }
            }
        }
    }
}

@Composable
fun TrendItem(trend: MovieTrend) {
    ListItem(
        leadingContent = {
            Text(
                text = trend.rank,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        headlineContent = { Text(trend.title, fontWeight = FontWeight.Bold) },
        supportingContent = {
            if (trend.image != null) {
                AsyncImage(
                    model = trend.image,
                    contentDescription = null,
                    modifier = Modifier.height(100.dp).width(70.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    )
    HorizontalDivider()
}
