package com.example.coroutinepractice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.LocalDateTime

class MainViewModel: ViewModel() {
    private val _nowState = MutableStateFlow(LocalDateTime.now().toString())
    val nowState: StateFlow<String> = _nowState

    private val _delayedState = MutableStateFlow(LocalDateTime.now().toString())
    val  delayedState : StateFlow<String> = _delayedState

    fun updateNow(){
        _nowState.value = LocalDateTime.now().toString()
    }
    fun updateDelayed() {
        viewModelScope.launch(Dispatchers.IO){
            delay(3000)
            _delayedState.value = LocalDateTime.now().toString()
        }
    }

}