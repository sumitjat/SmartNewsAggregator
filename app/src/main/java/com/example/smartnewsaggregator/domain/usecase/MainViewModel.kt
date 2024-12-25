package com.example.smartnewsaggregator.domain.usecase

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartnewsaggregator.data.repository.NewsRepository
import com.example.smartnewsaggregator.domain.model.ApiResult
import com.example.smartnewsaggregator.domain.model.Article
import com.example.smartnewsaggregator.domain.repository.NewsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepositoryImpl
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<ApiResult<List<Article>>>(ApiResult.Loading)    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<ApiResult<List<Article>>> = _uiState

    init {
        fetchTopHeadlines()
    }

    fun fetchTopHeadlines(): Flow<ApiResult<List<Article>>> = flow {
        viewModelScope.launch {
            newsRepository.getTopHeadlines().collect { collect ->
                _uiState.value =  collect
            }
        }
    }
}