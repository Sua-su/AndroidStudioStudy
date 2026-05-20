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
    private val repo: MovieRepository // 데이터를 가져오는 창고(레포지토리)를 주입받아요!
) : ViewModel() {

    // 화면에 보여줄 영화 목록들을 저장하는 상자입니다!
    // Compose가 이 상자의 변화를 감시하고 있다가 값이 바뀌면 화면을 다시 그려줘요.
    var nowPlayingMovies by mutableStateOf<List<MovieDto>>(emptyList())
        private set

    var popularMovies by mutableStateOf<List<MovieDto>>(emptyList())
        private set

    // 지금 데이터를 불러오는 중인지 체크하는 변수입니다.
    var isLoading by mutableStateOf(false)
        private set

    // 통신하다가 에러가 나면 여기에 메시지를 담아둘 거에요.
    var errorMessage by mutableStateOf("")
        private set

    init {
        // 앱이 켜지고 뷰모델이 만들어지자마자 영화 데이터를 가져오기 위해 호출합니다!
        loadMovies()
    }

    fun loadMovies() {
        // 네트워크 통신은 시간이 걸리니까 '코루틴'이라는 비동기 방식으로 작업해요.
        viewModelScope.launch {
            isLoading = true // 작업 시작하니까 로딩 상태를 '참(true)'으로 바꿉니다.
            errorMessage = "" // 에러 메시지는 비워두고요.

            try {
                // 레포지토리한테 영화 목록 가져오라고 시킵니다!
                nowPlayingMovies = repo.getNowPlayingMovies()
                popularMovies = repo.getPopularMovies()
            } catch (e: Exception) {
                // 에러가 나면 catch 블록으로 와서 에러 메시지를 작성해줍니다.
                errorMessage = "영화 정보를 가져오는 중에 문제가 생겼어요: " + e.message
            } finally {
                // 성공하든 실패하든 이제 로딩은 끝났으니 '거짓(false)'으로 바꿉니다.
                isLoading = false
            }
        }
    }
}
