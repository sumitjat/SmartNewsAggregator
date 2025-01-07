package com.example.smartnewsaggregator.domain.model

import java.util.Date

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: Date,
    val isBookmarked: Boolean
)
