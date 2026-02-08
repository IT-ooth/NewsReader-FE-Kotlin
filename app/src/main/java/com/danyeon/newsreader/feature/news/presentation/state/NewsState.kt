package com.danyeon.newsreader.feature.news.presentation.state

import com.danyeon.newsreader.feature.news.data.entity.NewsArticle

sealed class NewsState {
    object Idle: NewsState()
    object Loading: NewsState()
    data class Success(val data: List<NewsArticle>): NewsState()
    data class Error(val msg: String): NewsState()
}