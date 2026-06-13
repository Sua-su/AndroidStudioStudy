package com.example.tmdb.ui.trend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.data.repository.TrendRepository
import com.example.tmdb.model.MovieTrend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendViewModel @Inject constructor(
    private val trendRepository: TrendRepository
) : ViewModel() {

    private val _trends = MutableStateFlow<List<MovieTrend>>(emptyList())
    val trends: StateFlow<List<MovieTrend>> = _trends

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchTrends()
    }

    fun fetchTrends() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _trends.value = trendRepository.getBoxOfficeTrends()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
