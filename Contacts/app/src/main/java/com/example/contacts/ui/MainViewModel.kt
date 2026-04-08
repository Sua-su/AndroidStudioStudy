package com.example.contacts.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {
    private val _keywordState = MutableStateFlow("")
    val keywordState: StateFlow<String> = _keywordState

    fun  updateKeyword(value: String){
        _keywordState.value = value
    }
}