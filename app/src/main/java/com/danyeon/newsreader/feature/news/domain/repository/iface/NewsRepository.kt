package com.danyeon.newsreader.feature.news.domain.repository.iface

import com.danyeon.newsreader.core.network.requests.CardNewsRequest
import com.danyeon.newsreader.feature.news.data.entity.NewsArticle

interface NewsRepository {
    suspend fun getNews(request: CardNewsRequest): List<NewsArticle>
}