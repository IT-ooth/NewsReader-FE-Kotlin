package com.danyeon.newsreader.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val source: String,
    val url: String,
    val title: String,
    val summary: String,
    val themes: String,
    val level: String,
    val category: String
)