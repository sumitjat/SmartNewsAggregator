package com.example.smartnewsaggregator

import android.app.Application
import com.example.smartnewsaggregator.domain.repository.NewsUpdateService
import com.example.smartnewsaggregator.service.NewsUpdateWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltAndroidApp
class SmartNews : Application() {

    val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        NewsUpdateWorker.schedule(this)
        scope.launch {
            delay(10000)
            NewsUpdateService.startService(this@SmartNews)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        NewsUpdateService.stopService(this)
        NewsUpdateWorker.cancel(this)
        scope.cancel()
    }
}