package com.example.smartnewsaggregator.domain.model

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleResponse>,
    val code: String? = null,
    val message: String? = null
)
