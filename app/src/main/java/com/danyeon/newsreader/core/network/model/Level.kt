package com.danyeon.newsreader.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Level(val level: String) {
    @SerialName("Low")
    LOW("Low"),
    @SerialName("Medium")
    MEDIUM("Medium"),
    @SerialName("High")
    HIGH("High");

    companion object {
        fun fromString(value: String) : Level =
            entries.find { it.level == value } ?: LOW
    }
}