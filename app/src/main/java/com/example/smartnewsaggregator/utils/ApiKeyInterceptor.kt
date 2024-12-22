package com.example.smartnewsaggregator.utils

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .url(
                    chain.request().url.newBuilder()
                        .addQueryParameter("apiKey", "535b5a880a094a18b6eae0dac5bd1ad1").build()
                )
                .build()
        )
    }
}