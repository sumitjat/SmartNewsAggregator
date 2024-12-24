package com.example.smartnewsaggregator.domain.model

import androidx.room.Entity

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleResponse>,
    val code: String? = null,
    val message: String? = null
)
