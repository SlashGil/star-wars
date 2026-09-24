package com.slashgil.starwars.domain.contract

data class Film(
    val url: String,
    val title: String,
    val posterUrl: String
) {
    val id: String?
        get() {
            val trimmed = url.trimEnd('/')
            val idStr = trimmed.substringAfterLast('/')
            return idStr.ifEmpty { null }
        }
}
