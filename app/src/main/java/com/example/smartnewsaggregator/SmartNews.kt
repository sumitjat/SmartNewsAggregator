package com.example.smartnewsaggregator

import android.app.Application
import androidx.room.Room
import com.example.smartnewsaggregator.data.local.NewsDatabase
import com.example.smartnewsaggregator.domain.repository.NewsUpdateService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltAndroidApp
class SmartNews : Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch(Dispatchers.IO) {
            delay(10000)
            NewsUpdateService.startService(this@SmartNews)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        NewsUpdateService.stopService(this)
    }
}