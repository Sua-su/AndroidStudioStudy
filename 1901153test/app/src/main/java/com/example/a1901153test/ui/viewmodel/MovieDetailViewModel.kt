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
    private val repo: MovieRepository,
    savedState: SavedStateHandle // 다른 화면에서 보낸 영화 아이디(movieId)를 받기 위한 도구에요!
) : ViewModel() {

    // 넘겨받은 영화 아이디를 꺼내봅니다. 없으면 기본값으로 0을 넣어요.
    private val movieId: Int = savedState.get<Int>("movieId") ?: 0

    // 영화 상세 정보 한 개를 저장할 상자에요. 처음엔 데이터가 없으니 null로 시작해요.
    var movie by mutableStateOf<MovieDetailDto?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    init {
        // 뷰모델이 생기자마자 이 영화가 어떤 영화인지 상세 정보를 가져옵니다!
        loadMovieDetail()
    }

    fun loadMovieDetail() {
        // 상세 정보를 가져오기 위해 코루틴(비동기 작업)을 실행합니다.
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            
            try {
                // 레포지토리에 영화 아이디를 알려주면서 상세 정보를 요청해요.
                movie = repo.getMovieDetail(movieId)
            } catch (e: Exception) {
                // 에러가 나면 사용자에게 보여줄 메시지를 적습니다.
                errorMessage = "상세 정보를 불러올 수 없습니다. 인터넷 연결을 확인해보세요!"
            } finally {
                // 작업이 끝났으니 로딩을 끕니다.
                isLoading = false
            }
        }
    }
}
