package com.example.postsv2.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postsv2.data.dto.PostDto
import com.example.postsv2.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PostListUiState {
    data object Loading : PostListUiState
    data class Success(val postDtos: List<PostDto>) : PostListUiState
    data class Error(val message: String) : PostListUiState
}

class PostListViewModel(
    private val repository: PostRepository = PostRepositoryRemoteImpl()
) : ViewModel() {
    private val _uiState: MutableStateFlow<PostListUiState> = MutableStateFlow(PostListUiState.Loading)
    val uiState: StateFlow<PostListUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = PostListUiState.Loading
            try {
                val posts = repository.getPosts()
                _uiState.value = PostListUiState.Success(posts)
            } catch (e: Exception) {
                _uiState.value = PostListUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다")
            }
        }
    }
}
