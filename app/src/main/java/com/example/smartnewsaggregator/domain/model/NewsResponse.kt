package com.example.smartnewsaggregator.domain.model

import androidx.room.Entity

@Entity(tableName = "articles")
data class NewsResponse(
    val id: String = ""
)
