package com.example.tmdb.ui.board

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.data.repository.MovieRepository
import com.example.tmdb.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val posts: StateFlow<List<Post>> = repository.getAllPosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun addPost(title: String, content: String, author: String?) {
        if (title.isBlank() || content.isBlank()) {
            _error.value = "Title and Content cannot be empty"
            return
        }
        viewModelScope.launch {
            val userId = sharedPreferences.getLong("loggedInUserId", -1L)
            repository.addPost(
                Post(
                    title = title, 
                    content = content, 
                    authorNickname = author,
                    authorId = if (userId != -1L) userId else null
                )
            )
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            repository.deletePost(post)
        }
    }

    fun clearError() {
        _error.value = null
    }
}
