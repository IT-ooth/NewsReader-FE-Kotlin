package com.danyeon.newsreader.feature.news.domain.repository.impl

import com.danyeon.newsreader.core.network.api.NewsAPI
import com.danyeon.newsreader.core.network.requests.CardNewsRequest
import com.danyeon.newsreader.feature.news.data.entity.NewsArticle
import com.danyeon.newsreader.feature.news.data.entity.toDomain
import com.danyeon.newsreader.feature.news.domain.repository.iface.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsAPI
): NewsRepository {
    override suspend fun getNews(request: CardNewsRequest): List<NewsArticle> =
         api.getCardNews(request).map {
             it.toDomain()
         }
}