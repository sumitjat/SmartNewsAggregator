package com.example.smartnewsaggregator.utils

import com.example.smartnewsaggregator.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .url(
                    chain.request().url.newBuilder()
                        .addQueryParameter("apiKey", BuildConfig.API_KEY).build()
                )
                .build()
        )
    }
}