package com.example.a1901153test.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1901153test.data.MovieRepository
import com.example.a1901153test.data.model.MovieDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    // 현재상영 영화 목록
    var nowPlayingMovies by mutableStateOf<List<MovieDto>>(emptyList())
        private set

    // 인기 영화 목록
    var popularMovies by mutableStateOf<List<MovieDto>>(emptyList())
        private set

    // 로딩 상태
    var isLoading by mutableStateOf(false)
        private set

    // 에러 메시지
    var errorMessage by mutableStateOf("")
        private set

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                nowPlayingMovies = movieRepository.getNowPlayingMovies()
                popularMovies = movieRepository.getPopularMovies()
            } catch (e: Exception) {
                // 에러나면 메시지 보여주기
                errorMessage = "영화 목록을 불러오는데 실패했습니다.\n${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
