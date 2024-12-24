package com.example.smartnewsaggregator.data.repository

import com.example.smartnewsaggregator.domain.model.ApiResult
import com.example.smartnewsaggregator.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(): Flow<ApiResult<List<Article>>>
    suspend fun getBookmarkedArticles(): Flow<ApiResult<List<Article>>>
    suspend fun refreshNews()
    suspend fun toggleBookmark(articleId: String)
    suspend fun searchNews(query: String): Flow<ApiResult<List<Article>>>
}