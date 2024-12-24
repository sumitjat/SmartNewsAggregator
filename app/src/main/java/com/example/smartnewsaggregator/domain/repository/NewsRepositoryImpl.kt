package com.example.smartnewsaggregator.domain.repository

import com.example.smartnewsaggregator.data.remote.NewsApiService
import com.example.smartnewsaggregator.data.repository.NewsRepository
import com.example.smartnewsaggregator.domain.model.ApiResult
import com.example.smartnewsaggregator.domain.model.Article
import com.example.smartnewsaggregator.domain.model.ArticleMapper.toArticleList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService
) : NewsRepository {
    override suspend fun getTopHeadlines(): Flow<ApiResult<List<Article>>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = newsApiService.getTopHeadlines("us", "general")
            emit(ApiResult.Success(response.toArticleList()))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override suspend fun getBookmarkedArticles(): Flow<ApiResult<List<Article>>> = flow {
        emit(ApiResult.Loading)
    }

    override suspend fun refreshNews() {

    }

    override suspend fun toggleBookmark(articleId: String) {

    }

    override suspend fun searchNews(query: String): Flow<ApiResult<List<Article>>> {
       return flow {  }
    }

}