package com.slashgil.starwars.data.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanetDto(
    @SerialName("name") val name: String,
    @SerialName("rotation_period") val rotationPeriod: String = "",
    @SerialName("orbital_period") val orbitalPeriod: String = "",
    @SerialName("diameter") val diameter: String = "",
    @SerialName("climate") val climate: String = "",
    @SerialName("gravity") val gravity: String = "",
    @SerialName("terrain") val terrain: String = "",
    @SerialName("surface_water") val surfaceWater: String = "",
    @SerialName("population") val population: String = "",
    @SerialName("residents") val residents: List<String> = emptyList(),
    @SerialName("films") val films: List<String> = emptyList(),
    @SerialName("url") val url: String
)
