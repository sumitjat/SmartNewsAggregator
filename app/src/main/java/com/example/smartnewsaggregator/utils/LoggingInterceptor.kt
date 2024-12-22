package com.example.smartnewsaggregator.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("LoggingInterceptor", "Request URL: ${request.url}")
        Log.d("LoggingInterceptor", "Request URL: ${request.body}")
        Log.d("LoggingInterceptor", "Request URL: ${request.headers}")
        Log.d("LoggingInterceptor", "Request URL: ${request.method}")

        val response =  chain.proceed(request)

        Log.d("LoggingInterceptor", "Response URL: $response")
        Log.d("LoggingInterceptor", "Response URL: ${response.headers}")
        Log.d("LoggingInterceptor", "Response URL: ${response.code}")
        return response
    }
}