package com.danyeon.newsreader.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Category(name: String) {
    @SerialName("Tech")
    TECH("TECH");

    companion object {
        fun fromString(value: String): Category =
            entries.find { it.name == value } ?: TECH
    }
}