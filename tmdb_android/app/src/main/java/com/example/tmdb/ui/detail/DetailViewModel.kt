package com.example.tmdb.ui.detail

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.data.repository.MovieRepository
import com.example.tmdb.model.Movie
import com.example.tmdb.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val sharedPreferences: SharedPreferences,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = savedStateHandle.get<Int>("movieId") ?: 0

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie

    val reviews: StateFlow<List<Review>> = repository.getReviewsForMovie(movieId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchMovieDetails()
    }

    private fun fetchMovieDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _movie.value = repository.getMovieDetails(movieId)
            } catch (e: Exception) {
                _error.value = "Failed to load movie details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addReview(rating: Float, comment: String) {
        viewModelScope.launch {
            val movie = _movie.value ?: return@launch
            val nickname = sharedPreferences.getString("nickname", null)
            val userId = sharedPreferences.getLong("loggedInUserId", -1L)
            val review = Review(
                movieId = movieId,
                movieTitle = movie.title,
                rating = rating,
                comment = comment,
                authorNickname = nickname,
                authorId = if (userId != -1L) userId else null
            )
            repository.addReview(review)
        }
    }

    fun updateReview(review: Review) {
        viewModelScope.launch {
            repository.updateReview(review)
        }
    }

    fun deleteReview(review: Review) {
        viewModelScope.launch {
            repository.deleteReview(review)
        }
    }

    fun clearError() {
        _error.value = null
    }
}
