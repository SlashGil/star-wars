package com.slashgil.starwars.data.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilmDto(
    @SerialName("title") val title: String,
    @SerialName("episode_id") val episodeId: Int = 0,
    @SerialName("opening_crawl") val openingCrawl: String = "",
    @SerialName("director") val director: String = "",
    @SerialName("producer") val producer: String = "",
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("characters") val characters: List<String> = emptyList(),
    @SerialName("planets") val planets: List<String> = emptyList(),
    @SerialName("starships") val starships: List<String> = emptyList(),
    @SerialName("vehicles") val vehicles: List<String> = emptyList(),
    @SerialName("species") val species: List<String> = emptyList(),
    @SerialName("url") val url: String
)
