package com.slashgil.starwars.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    @SerialName("name") val name: String,
    @SerialName("height") val height: String,
    @SerialName("mass") val mass: String,
    @SerialName("hair_color") val hairColor: String,
    @SerialName("skin_color") val skinColor: String,
    @SerialName("eye_color") val eyeColor: String,
    @SerialName("birth_year") val birthYear: String,
    @SerialName("gender") val gender: String,
    @SerialName("homeworld") val homeworld: String,
    @SerialName("url") val url: String,
    @SerialName("films") val films: List<String> = emptyList()
)
