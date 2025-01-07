package com.example.smartnewsaggregator.domain.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Mapper to convert API response to domain model
object ArticleMapper {

    // Extension function to convert API response to Entity
    fun ArticleResponse.toEntity() = ArticleEntity(
        url = url,
        title = title,
        description = description,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        author = author,
        sourceName = source.name,
        content = content,
        lastUpdated = System.currentTimeMillis()
    )

    // Extension function to convert Entity to Domain model
    fun ArticleEntity.toDomain() = Article(
        id = url,
        title = title,
        description = description ?: "",
        url = url,
        imageUrl = urlToImage ?: "",
        publishedAt = parseDateTime(publishedAt),
        isBookmarked = false,
    )

    private fun parseDateTime(dateString: String): Date {
        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .parse(dateString) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

