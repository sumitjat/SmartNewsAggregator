package com.example.smartnewsaggregator.domain.usecase

import com.example.smartnewsaggregator.domain.repository.NewsRepositoryImpl
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartnewsaggregator.domain.model.ApiResult
import com.example.smartnewsaggregator.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepositoryImpl
) : ViewModel() {

    private val _newsState = MutableStateFlow<ApiResult<List<Article>>>(ApiResult.Loading)
    val newsState: StateFlow<ApiResult<List<Article>>> = _newsState.asStateFlow()

    init {
        refreshNews()
        observeNews()
    }

    private fun observeNews() {
        viewModelScope.launch {
            newsRepository.getLatestNews()
                .collect { articles ->
                    _newsState.value = ApiResult.Success(articles)
                }
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            _newsState.value = ApiResult.Loading
            try {
                newsRepository.refreshNews()
            } catch (e: Exception) {
                _newsState.value = ApiResult.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}