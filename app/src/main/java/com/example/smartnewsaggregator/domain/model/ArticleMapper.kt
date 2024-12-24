package com.example.smartnewsaggregator.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Mapper to convert API response to domain model
object ArticleMapper {
    fun ArticleResponse.toDomainModel(): Article {
        return Article(
            id = url, // Using URL as unique identifier
            title = title,
            description = description ?: "",
            url = url,
            imageUrl = urlToImage ?: "",
            publishedAt = parseDateTime(publishedAt),
//            category = NewsCategory.GENERAL, // Default category, can be updated based on source
            isBookmarked = false
        )
    }

    private fun parseDateTime(dateString: String): Date {
        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .parse(dateString) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    // Extension function to handle API response
    fun NewsResponse.toArticleList(): List<Article> {
        return if (status == "ok") {
            articles.map { it.toDomainModel() }
        } else {
            emptyList()
        }
    }
}

