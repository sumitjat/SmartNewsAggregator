package com.example.smartnewsaggregator.domain.model

// Article response model
data class ArticleResponse(
    val source: SourceResponse,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)
