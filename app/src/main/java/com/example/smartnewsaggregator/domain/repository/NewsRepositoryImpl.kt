package com.example.smartnewsaggregator.domain.repository

import android.content.Context
import android.util.Log
import com.example.smartnewsaggregator.data.local.NewsDao
import com.example.smartnewsaggregator.data.remote.NewsApiService
import com.example.smartnewsaggregator.data.repository.NewsRepository
import com.example.smartnewsaggregator.domain.model.ApiResult
import com.example.smartnewsaggregator.domain.model.Article
import com.example.smartnewsaggregator.domain.model.ArticleMapper.isNetworkAvailable
import com.example.smartnewsaggregator.domain.model.ArticleMapper.toDomain
import com.example.smartnewsaggregator.domain.model.ArticleMapper.toEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao,
    @ApplicationContext private val context: Context
) : NewsRepository {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun refreshNews() {
        withContext(ioDispatcher) {
            try {
                // Check network connectivity
                if (!isNetworkAvailable(context)) {
                    throw NoNetworkException()
                }

                // Fetch from API
                val response = newsApiService.getTopHeadlines("us","general")

                // Convert and save to database
                val articles = response.articles.map { it.toEntity() }
                newsDao.insertArticles(articles)

                // Clean up old articles (older than 24 hours)
                val dayAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
                newsDao.deleteOldArticles(dayAgo)
            } catch (e: Exception) {
                throw when (e) {
                    is IOException -> NetworkException(e)
                    is HttpException -> ApiException(e)
                    else -> e
                }
            }
        }
    }

    override suspend fun toggleBookmark(articleId: String) {

    }

    override suspend fun searchNews(query: String): Flow<ApiResult<List<Article>>> {
        return flow {  }
    }

    override fun getLatestNews(): Flow<List<Article>> {
        return newsDao.getAllArticles()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .catch { e ->
                emit(emptyList())
                Log.e("NewsRepository", "Error fetching news", e)
            }
            .flowOn(ioDispatcher)
    }

    override fun getBreakingNews(): Flow<List<Article>> {
        val threeHoursAgo = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3)
        return newsDao.getBreakingNews(threeHoursAgo)
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .flowOn(ioDispatcher)
    }
}

// Custom Exceptions
class NoNetworkException : Exception("No network connection available")
class NetworkException(cause: Throwable) : Exception("Network error occurred", cause)
class ApiException(cause: Throwable) : Exception("API error occurred", cause)