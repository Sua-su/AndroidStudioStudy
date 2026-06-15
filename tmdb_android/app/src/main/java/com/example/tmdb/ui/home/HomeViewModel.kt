package com.example.tmdb.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.data.repository.MovieRepository
import com.example.tmdb.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var currentPage = 1
    private var isLastPage = false

    init {
        fetchPopularMovies()
    }

    fun fetchPopularMovies() {
        if (_isLoading.value) return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val newMovies = repository.getPopularMovies(currentPage)
                if (newMovies.isEmpty()) {
                    isLastPage = true
                } else {
                    _movies.value = if (currentPage == 1) newMovies else _movies.value + newMovies
                }
            } catch (e: Exception) {
                _error.value = "Failed to load movies: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMore() {
        if (isLoading.value || isLastPage) return
        currentPage++
        fetchPopularMovies()
    }

    fun clearError() {
        _error.value = null
    }
}
