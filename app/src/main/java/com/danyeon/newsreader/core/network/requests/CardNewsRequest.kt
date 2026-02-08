package com.danyeon.newsreader.core.network.requests

import com.danyeon.newsreader.core.network.model.Category
import com.danyeon.newsreader.core.network.model.Level
import kotlinx.serialization.Serializable

@Serializable
data class CardNewsRequest(
    val category: Category,
    val level: Level,
    val offset: Int,
    val limit: Int
)