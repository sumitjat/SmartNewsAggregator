package com.example.smartnewsaggregator.domain.repository

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.smartnewsaggregator.MainActivity
import com.example.smartnewsaggregator.data.repository.NewsRepository
import com.example.smartnewsaggregator.domain.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NOTIFICATION_CHANNEL_ID = "news_service_channel"
private const val NOTIFICATION_ID = 1
private const val SERVICE_ID = 1

@AndroidEntryPoint
class NewsUpdateService : Service() {

    @Inject
    lateinit var newsRepository: NewsRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                SERVICE_ID,
                createNotification("News Service Running"),
                FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        }
    }

    private fun createNotification(notificationContent: String): Notification {
        val pendingIntent = createPendingIntent()

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("News Service")
            .setContentText(notificationContent)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .build()

    }

    private fun createPendingIntent(): PendingIntent {
        val intent = packageManager
            .getLaunchIntentForPackage(packageName)
            ?.apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_FETCH -> handleNewsFetch()
            ACTION_CHECK_BREAKING_NEWS -> checkBreakingNews()
            ACTION_STOP_SERVICE -> stopService()
        }
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun handleNewsFetch() {
        serviceScope.launch {
            Log.d("NewsUpdateService", "Checking breaking news")
            try {
                updateNotification("Fetching latest news")
                newsRepository.refreshNews()
            } catch (e: Exception) {
                updateNotification("Fetching news check failed: ${e.localizedMessage}")
            }
        }
    }

    private fun checkBreakingNews() {
        serviceScope.launch {
            Log.d("NewsUpdateService", "Checking breaking news")
            try {
                async {
                    newsRepository.refreshNews()
                }.await()

                async {
                    newsRepository.getLatestNews()
                        .catch { e ->
                            updateNotification("Error checking breaking news: ${e.localizedMessage}")
                        }
                        .collect { breakingNews ->
                            breakingNews.firstOrNull()?.let {
                                showBreakingNewsNotification(it)
                            }
                        }
                }.await()


            } catch (e: Exception) {
                updateNotification("Breaking news check failed: ${e.localizedMessage}")
            }
        }
    }

    private fun showBreakingNewsNotification(article: Article) {
        Log.d("NewsUpdateService", "Showing breaking news notification: $article")
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("article_id", article.id)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Breaking News!")
            .setContentText(article.title)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1234, notification)
    }


    private fun updateNotification(content: String) {
        Log.d("NewsUpdateService", "Updating notification: $content")
        val notification = createNotification(content)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun stopService() {
        serviceScope.cancel()
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    private fun createNotificationChannel() {
        Log.d("NewsUpdateService", "Creating notification channel")
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "News Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    companion object {
        const val ACTION_START_FETCH = "action_start_fetch"
        const val ACTION_CHECK_BREAKING_NEWS = "action_check_breaking_news"
        const val ACTION_STOP_SERVICE = "action_stop_service"

        fun startService(context: Context) {
            val intent = Intent(context, NewsUpdateService::class.java).apply {
                action = ACTION_CHECK_BREAKING_NEWS
            }

            context.startForegroundService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, NewsUpdateService::class.java).apply {
                action = ACTION_STOP_SERVICE
            }
            context.startService(intent)
        }
    }
}