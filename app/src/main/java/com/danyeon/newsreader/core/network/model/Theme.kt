package com.danyeon.newsreader.core.network.model

enum class Theme(name: String) {
    SECURITY("Security"),
    AIML("AI/ML");

    companion object {
        fun fromString(value: String): List<Theme> {
            return value.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { raw ->
                    entries.find { it.name.equals(raw, ignoreCase = true) } ?: SECURITY
                }
                .distinct()
        }
    }
}