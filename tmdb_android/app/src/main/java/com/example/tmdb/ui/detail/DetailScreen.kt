package com.example.tmdb.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tmdb.model.Movie
import com.example.tmdb.model.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val movie by viewModel.movie.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var editingReview by remember { mutableStateOf<Review?>(null) }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(movie?.title ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            movie?.let { m ->
                LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                    item { MovieHeader(m) }
                    item {
                        ReviewForm(
                            existingReview = editingReview,
                            onAddReview = viewModel::addReview,
                            onUpdateReview = {
                                viewModel.updateReview(it)
                                editingReview = null
                            },
                            onCancelEdit = { editingReview = null }
                        )
                    }
                    item { Text("Reviews", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp)) }
                    items(reviews) { review ->
                        ReviewItem(
                            review = review,
                            onDelete = { viewModel.deleteReview(review) },
                            onEdit = { editingReview = review }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieHeader(movie: Movie) {
    Column {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp, max = 450.dp),
            contentScale = ContentScale.Fit
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Release Date: ${movie.releaseDate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Rating: ${movie.rating}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = movie.overview, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ReviewForm(
    existingReview: Review? = null,
    onAddReview: (Float, String) -> Unit,
    onUpdateReview: (Review) -> Unit,
    onCancelEdit: () -> Unit
) {
    var rating by remember(existingReview) { mutableStateOf(existingReview?.rating ?: 5f) }
    var comment by remember(existingReview) { mutableStateOf(existingReview?.comment ?: "") }

    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                if (existingReview != null) "Edit Review" else "Add a Review",
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = rating,
                onValueChange = { rating = it },
                valueRange = 1f..10f,
                steps = 8
            )
            Text("Rating: ${rating.toInt()}")
            TextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Write your review...") }
            )
            Row(modifier = Modifier.align(Alignment.End).padding(top = 8.dp)) {
                if (existingReview != null) {
                    TextButton(onClick = onCancelEdit) {
                        Text("Cancel")
                    }
                }
                Button(
                    onClick = {
                        if (comment.isNotBlank()) {
                            if (existingReview != null) {
                                onUpdateReview(existingReview.copy(rating = rating, comment = comment))
                            } else {
                                onAddReview(rating, comment)
                                comment = ""
                            }
                        }
                    }
                ) {
                    Text(if (existingReview != null) "Update" else "Submit")
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review, onDelete: () -> Unit, onEdit: () -> Unit) {
    ListItem(
        headlineContent = { Text("Rating: ${review.rating.toInt()}") },
        supportingContent = { Text(review.comment) },
        trailingContent = {
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    )
}
