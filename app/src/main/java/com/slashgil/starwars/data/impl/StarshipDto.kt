package com.slashgil.starwars.data.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StarshipDto(
    @SerialName("name") val name: String,
    @SerialName("model") val model: String = "",
    @SerialName("manufacturer") val manufacturer: String = "",
    @SerialName("cost_in_credits") val costInCredits: String = "",
    @SerialName("length") val length: String = "",
    @SerialName("max_atmosphering_speed") val maxAtmospheringSpeed: String = "",
    @SerialName("crew") val crew: String = "",
    @SerialName("passengers") val passengers: String = "",
    @SerialName("cargo_capacity") val cargoCapacity: String = "",
    @SerialName("consumables") val consumables: String = "",
    @SerialName("hyperdrive_rating") val hyperdriveRating: String = "",
    @SerialName("MGLT") val mglt: String = "",
    @SerialName("starship_class") val starshipClass: String = "",
    @SerialName("pilots") val pilots: List<String> = emptyList(),
    @SerialName("films") val films: List<String> = emptyList(),
    @SerialName("url") val url: String
)
