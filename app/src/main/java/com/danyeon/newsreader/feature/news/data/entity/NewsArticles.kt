package com.danyeon.newsreader.feature.news.data.entity

import com.danyeon.newsreader.core.network.model.Category
import com.danyeon.newsreader.core.network.model.Level
import com.danyeon.newsreader.core.network.model.Theme
import com.danyeon.newsreader.core.network.response.NewsResponse

data class NewsArticle(
    val source: String,
    val url: String,
    val title: String,
    val theme: List<Theme>,
    val level: Level,
    val category: Category
)

fun NewsResponse.toDomain() : NewsArticle{
    return NewsArticle(
        source = this.source,
        url = this.url,
        title = this.title,
        theme = Theme.fromString(this.themes),
        level = Level.fromString(this.level),
        category = Category.fromString(this.category)
    )
}