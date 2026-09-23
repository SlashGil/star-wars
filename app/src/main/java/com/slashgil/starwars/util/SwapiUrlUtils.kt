package com.slashgil.starwars.util

object SwapiUrlUtils {
    fun extractPersonId(url: String): String? {
        val trimmed = url.trimEnd('/')
        val id = trimmed.substringAfterLast('/')
        return id.ifEmpty { null }
    }

    fun getPersonImageUrl(url: String): String {
        val id = extractPersonId(url)
        return if (id != null) {
            "https://starwars-visualguide.com/assets/img/characters/$id.jpg"
        } else {
            ""
        }
    }

    fun extractFilmId(filmUrl: String): String? {
        val trimmed = filmUrl.trimEnd('/')
        val id = trimmed.substringAfterLast('/')
        return id.ifEmpty { null }
    }

    fun getFilmImageUrl(filmUrl: String): String {
        val id = extractFilmId(filmUrl)
        return if (id != null) {
            "https://starwars-visualguide.com/assets/img/films/$id.jpg"
        } else {
            ""
        }
    }

    fun getFilmTitle(filmUrl: String): String {
        val id = extractFilmId(filmUrl) ?: return "Film"
        return when (id) {
            "1" -> "Episode IV: A New Hope"
            "2" -> "Episode V: The Empire Strikes Back"
            "3" -> "Episode VI: Return of the Jedi"
            "4" -> "Episode I: The Phantom Menace"
            "5" -> "Episode II: Attack of the Clones"
            "6" -> "Episode III: Revenge of the Sith"
            "7" -> "Episode VII: The Force Awakens"
            else -> "Episode $id"
        }
    }
}
