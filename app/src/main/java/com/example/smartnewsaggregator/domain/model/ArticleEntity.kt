package com.example.smartnewsaggregator.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val category: String,
    val isBookmarked: Boolean = false
)