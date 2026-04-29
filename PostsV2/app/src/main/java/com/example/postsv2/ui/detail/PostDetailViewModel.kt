package com.example.postsv2.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.postsv2.data.dto.PostDto
import com.example.postsv2.data.remote.RetrofitInstance
import com.example.postsv2.data.repository.PostRepository
import com.example.postsv2.data.repository.PostRepositoryRemoteImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PostDetailUiState {
    data object Loading : PostDetailUiState
    data class Success(val postDto: PostDto) : PostDetailUiState
    data class Error(val message: String) : PostDetailUiState
}

class PostDetailViewModel(
    private val postId: Int,
    private val repository: PostRepository = PostRepositoryRemoteImpl()
) : ViewModel()

    private val _uiState: MutableStateFlow<PostDetailUiState> = MutableStateFlow(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()
    init {
        loadPost()
    }

    private fun loadPost() {
        viewModelScope.launch {
            try {
                val post = repository.getPost(postId)
                _uiState.value = PostDetailUiState.Success(post)
            } catch (e: Exception) {
                _uiState.value = PostDetailUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다")
            }
        }
    }

    companion object {
        fun factory(postId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer { PostDetailViewModel(postId) }
        }
    }
}
