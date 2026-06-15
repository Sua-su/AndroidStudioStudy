package com.example.tmdb.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _authSuccess = MutableStateFlow(false)
    val authSuccess: StateFlow<Boolean> = _authSuccess

    fun login(username: String, passwordHash: String) {
        if (username.isBlank() || passwordHash.isBlank()) {
            _error.value = "Username and password cannot be empty"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(username, passwordHash)
            if (result.isSuccess) {
                _authSuccess.value = true
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Login failed"
            }
            _isLoading.value = false
        }
    }

    fun signup(username: String, passwordHash: String, nickname: String) {
        if (username.isBlank() || passwordHash.isBlank() || nickname.isBlank()) {
            _error.value = "All fields are required"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.signup(username, passwordHash, nickname)
            if (result.isSuccess) {
                // Auto-login after signup
                login(username, passwordHash)
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Signup failed"
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
