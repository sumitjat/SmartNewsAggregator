package com.example.smartnewsaggregator.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// ArticleEntity.kt
@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val url: String,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val author: String?,
    val sourceName: String,
    val content: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)