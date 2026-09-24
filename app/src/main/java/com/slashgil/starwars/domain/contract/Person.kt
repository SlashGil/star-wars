package com.slashgil.starwars.domain.contract

data class Person(
    val name: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String,
    val eyeColor: String,
    val birthYear: String,
    val gender: String,
    val homeworld: String,
    val url: String,
    val films: List<String> = emptyList()
) {
    val characterId: String?
        get() {
            val trimmed = url.trimEnd('/')
            val id = trimmed.substringAfterLast('/')
            return id.ifEmpty { null }
        }

    val imageUrl: String
        get() {
            val id = characterId
            return if (id != null) {
                "https://starwars-visualguide.com/assets/img/characters/$id.jpg"
            } else {
                ""
            }
        }
}

fun Person.toEntity(): StarWarsEntity.PersonEntity = StarWarsEntity.PersonEntity(
    name = name,
    height = height,
    mass = mass,
    hairColor = hairColor,
    skinColor = skinColor,
    eyeColor = eyeColor,
    birthYear = birthYear,
    gender = gender,
    homeworld = homeworld,
    url = url,
    films = films
)

