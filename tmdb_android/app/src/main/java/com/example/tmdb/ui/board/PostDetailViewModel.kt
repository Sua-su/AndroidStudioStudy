package com.example.tmdb.ui.board

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.data.repository.MovieRepository
import com.example.tmdb.model.Comment
import com.example.tmdb.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val sharedPreferences: SharedPreferences,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val postId: Int = savedStateHandle.get<Int>("postId") ?: 0

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post

    val comments: StateFlow<List<Comment>> = repository.getCommentsForPost(postId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadPost()
    }

    private fun loadPost() {
        viewModelScope.launch {
            _post.value = repository.getPostById(postId.toLong())
        }
    }

    fun addComment(nickname: String?, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            val userId = sharedPreferences.getLong("loggedInUserId", -1L)
            repository.addComment(
                Comment(
                    postId = postId,
                    authorNickname = nickname,
                    authorId = if (userId != -1L) userId else null,
                    content = content
                )
            )
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            repository.deleteComment(comment)
        }
    }
}
