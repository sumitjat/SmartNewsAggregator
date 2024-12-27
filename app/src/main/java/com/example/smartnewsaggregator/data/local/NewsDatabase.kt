package com.example.smartnewsaggregator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartnewsaggregator.domain.model.ArticleEntity

// NewsDatabase.kt
@Database(
    entities = [ArticleEntity::class],
    version = 1
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}