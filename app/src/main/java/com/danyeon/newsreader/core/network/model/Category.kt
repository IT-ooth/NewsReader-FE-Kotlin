package com.danyeon.newsreader.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Category() {
    @SerialName("Tech")
    TECH,

    ALL;
    companion object {
        fun fromString(value: String): Category =
            entries.find { it.name == value } ?: TECH
    }
}