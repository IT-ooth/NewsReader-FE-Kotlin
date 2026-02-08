package com.danyeon.newsreader.core.network.requests

import com.danyeon.newsreader.core.network.model.SearchType
import com.danyeon.newsreader.core.network.model.Theme
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    @SerialName("search_type")
    val searchType: SearchType,
    val themes: List<Theme>,
    val offset: Int,
    val limit: Int
)
