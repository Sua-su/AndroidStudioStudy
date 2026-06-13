package com.example.tmdb.ui.reviews

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tmdb.ui.detail.ReviewItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    onReviewClick: (Int) -> Unit,
    viewModel: ReviewsViewModel = hiltViewModel()
) {
    val reviews by viewModel.reviews.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Reviews") }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(reviews) { review ->
                ReviewItem(
                    review = review,
                    onDelete = { viewModel.deleteReview(review) },
                    onEdit = { onReviewClick(review.movieId) }
                )
            }
        }
    }
}
