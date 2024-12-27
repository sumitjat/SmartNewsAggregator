package com.example.smartnewsaggregator

import android.app.Application
import androidx.room.Room
import com.example.smartnewsaggregator.data.local.NewsDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class SmartNews : Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                applicationContext,
                NewsDatabase::class.java, "database-name"
            ).build()
        }
    }
}