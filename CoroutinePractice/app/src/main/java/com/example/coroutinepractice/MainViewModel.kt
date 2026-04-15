package com.example.coroutinepractice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutinepractice.data.dto.PostDto
import com.example.coroutinepractice.data.remote.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _postsState = MutableStateFlow<List<PostDto>>(emptyList())
    val postState: StateFlow<List<PostDto>> = _postsState
    fun fetchPosts(){
        try{
            viewModelScope.launch(Dispatchers.IO) {
                _postsState.value = NetworkModule.postApi.getPosts()
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}