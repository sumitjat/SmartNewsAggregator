package com.example.smartnewsaggregator.di

import com.example.smartnewsaggregator.utils.ApiKeyInterceptor
import com.example.smartnewsaggregator.utils.LoggingInterceptor
import com.example.smartnewsaggregator.data.remote.NewsApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = "https://newsapi.org/v2/"


    @Provides
    @Singleton
    fun providesMoshi(): MoshiConverterFactory {
        val adapterFactory: KotlinJsonAdapterFactory = KotlinJsonAdapterFactory()
        val moshi = Moshi.Builder().add(adapterFactory).build()
        val moshiKotlin: MoshiConverterFactory = MoshiConverterFactory.create(moshi)
        return moshiKotlin

    }

    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(LoggingInterceptor())
            .build()
        return client

    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, moshi: MoshiConverterFactory, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(moshi)
        .client(client)
        .build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NewsApiService= retrofit.create(NewsApiService::class.java)

}