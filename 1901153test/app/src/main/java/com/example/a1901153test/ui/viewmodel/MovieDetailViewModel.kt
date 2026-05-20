package com.example.a1901153test.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1901153test.data.MovieRepository
import com.example.a1901153test.data.model.MovieDetailDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 네비게이션으로 넘어온 movieId
    private val movieId: Int = savedStateHandle.get<Int>("movieId") ?: 0

    var movie by mutableStateOf<MovieDetailDto?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    init {
        loadMovieDetail()
    }

    fun loadMovieDetail() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                movie = movieRepository.getMovieDetail(movieId)
            } catch (e: Exception) {
                errorMessage = "영화 정보를 불러오는데 실패했습니다."
            } finally {
                isLoading = false
            }
        }
    }
}
