package com.danyeon.newsreader.feature.news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danyeon.newsreader.core.network.model.Category
import com.danyeon.newsreader.core.network.model.Level
import com.danyeon.newsreader.core.network.requests.CardNewsRequest
import com.danyeon.newsreader.feature.news.data.entity.NewsArticle
import com.danyeon.newsreader.feature.news.domain.repository.iface.NewsRepository
import com.danyeon.newsreader.feature.news.presentation.state.NewsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repo: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewsState>(NewsState.Idle)
    val state = _state.asStateFlow()

    private var currentOffset = 0
    private val limit = 20
    private var isLastPage = false
    private var isNextPageLoading = false
    private val allNews = mutableListOf<NewsArticle>()

    init {
        getNews(isRefresh = true)
    }
    fun getNews(isRefresh: Boolean = false) {
        if (isRefresh) {
            currentOffset = 0
            isLastPage = false
            allNews.clear()
        }

        if (isLastPage || isNextPageLoading) return

        viewModelScope.launch {
            if (allNews.isEmpty()) _state.value = NewsState.Loading
            isNextPageLoading = true

            runCatching {
                repo.getNews(CardNewsRequest(
                    category = Category.TECH,
                    level = Level.LOW,
                    offset = currentOffset,
                    limit = limit
                ))
            }.onSuccess { newList ->
                isNextPageLoading = false

                allNews.addAll(newList)

                isLastPage = newList.size < limit

                currentOffset += limit

                _state.value = NewsState.Success(allNews.toList())
            }.onFailure {
                isNextPageLoading = false
                _state.value = NewsState.Error(it.message ?: "Network Error")
            }
        }
    }
}