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
    private val _selectedCategory = MutableStateFlow(Category.ALL)
    val selectedCategory = _selectedCategory.asStateFlow()

    private var currentOffset = 0
    private val limit = 20
    private var isLastPage = false
    private var isNextPageLoading = false
    private val allNews = mutableListOf<NewsArticle>()

    init {
        getNews(isRefresh = true)
    }

    fun updateCategory(category: Category) {
        if (_selectedCategory.value == category) return // 동일 카테고리 중복 호출 방지
        _selectedCategory.value = category
        fetchNews(isRefresh = true)
    }

    fun loadNextPage() {
        fetchNews(isRefresh = false)
    }

    private fun fetchNews(isRefresh: Boolean) {
        if (isRefresh) {
            currentOffset = 0
            isLastPage = false
            allNews.clear()
            _state.value = NewsState.Loading
        }

        if (isLastPage || isNextPageLoading) return

        viewModelScope.launch {
            isNextPageLoading = true

            // Category.ALL일 경우 서버 요청 시 category 파라미터를 null로 넘겨 전체 데이터를 받습니다.
            val requestCategory = if (_selectedCategory.value == Category.ALL) null else _selectedCategory.value

            runCatching {
                repo.getNews(CardNewsRequest(
                    category = requestCategory,
                    level = Level.LOW,
                    offset = currentOffset,
                    limit = limit
                ))
            }.onSuccess { newList ->
                isNextPageLoading = false
                isLastPage = newList.size < limit

                if (newList.isNotEmpty()) {
                    currentOffset += limit
                    allNews.addAll(newList)
                }

                _state.value = NewsState.Success(allNews.toList())
            }.onFailure {
                isNextPageLoading = false
                _state.value = NewsState.Error(it.message ?: "Network Error")
            }
        }
    }

    fun getNews(category: Category? = null, isRefresh: Boolean = false) {
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
                    category = category,
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