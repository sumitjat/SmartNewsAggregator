package com.example.smartnewsaggregator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartnewsaggregator.domain.model.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles WHERE lastUpdated < :timestamp")
    suspend fun deleteOldArticles(timestamp: Long)

    @Query("SELECT * FROM articles WHERE publishedAt >= :timestamp ORDER BY publishedAt DESC")
    fun getBreakingNews(timestamp: Long): Flow<List<ArticleEntity>>
}