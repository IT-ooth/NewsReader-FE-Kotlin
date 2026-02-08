package com.danyeon.newsreader.core.network.api

import com.danyeon.newsreader.core.network.requests.CardNewsRequest
import com.danyeon.newsreader.core.network.requests.SearchRequest
import com.danyeon.newsreader.core.network.response.NewsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NewsAPI {
    @POST("/v1/cardnews")
    suspend fun getCardNews(
        @Body request: CardNewsRequest
    ): List<NewsResponse>

    @POST("v1/search/theme")
    suspend fun searchByTheme(
        @Body request: SearchRequest
    ): List<NewsResponse>
}