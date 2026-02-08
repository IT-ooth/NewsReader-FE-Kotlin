package com.danyeon.newsreader.core.network.di

import com.danyeon.newsreader.core.network.api.NewsAPI
import com.danyeon.newsreader.core.network.logging.RequestIdInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASEURL = "https://api.danyeon.cloud"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(RequestIdInterceptor())
            .addInterceptor(logging)
            .build()
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json
    ): Retrofit{
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsAPI(retrofit: Retrofit): NewsAPI =
        retrofit.create(NewsAPI::class.java)
}