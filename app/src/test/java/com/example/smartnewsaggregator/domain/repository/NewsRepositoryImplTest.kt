package com.example.smartnewsaggregator.domain.repository

import android.content.Context
import com.example.smartnewsaggregator.data.local.NewsDao
import com.example.smartnewsaggregator.data.remote.NewsApiService
import com.example.smartnewsaggregator.data.repository.NewsRepository
import com.example.smartnewsaggregator.domain.model.ApiResult
import com.example.smartnewsaggregator.domain.model.ArticleEntity
import com.example.smartnewsaggregator.domain.model.ArticleResponse
import com.example.smartnewsaggregator.domain.model.NewsResponse
import com.example.smartnewsaggregator.domain.model.SourceResponse
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever


@RunWith(MockitoJUnitRunner::class)
class NewsRepositoryImplTest {
    @Mock
    private lateinit var newsApiService: NewsApiService

    @Mock
    private lateinit var newsDao: NewsDao

    @Mock
    private lateinit var context: Context

    private lateinit var newsRepository: NewsRepositoryImpl

    @Before
    fun setup() {
        newsRepository = NewsRepositoryImpl(newsApiService, newsDao, context)
    }

    @Test
    fun `get top Headlines success returns Api Results Success`() = runBlocking {
        val articles = listOf(
            ArticleResponse(
                title = "title1",
                description = "description1",
                url = "url1",
                urlToImage = "urlToImage1",
                publishedAt = "publishedAt1",
                content = "content1",
                author = "author1",
                source = SourceResponse(id = "id1", name = "name1")
            )
        )

        whenever(newsApiService.getTopHeadlines("us", "general")).thenReturn(
            NewsResponse(
                articles = articles,
                status = "ok",
                totalResults = 1,
                code = null,
                message = null
            )
        )

//        whenever(
//            context.
//        )

        val result = newsRepository.refreshNews()
    }

}