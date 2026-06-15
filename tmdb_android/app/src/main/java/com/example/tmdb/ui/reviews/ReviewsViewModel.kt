package com.example.tmdb.ui.reviews

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.data.repository.MovieRepository
import com.example.tmdb.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val repository: MovieRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    val reviews: StateFlow<List<Review>> = repository.getReviewsByUserId(sharedPreferences.getLong("loggedInUserId", -1L))
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteReview(review: Review) {
        viewModelScope.launch {
            repository.deleteReview(review)
        }
    }
}
