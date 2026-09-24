package com.slashgil.starwars.domain.contract

sealed class StarWarsEntity {
    abstract val name: String
    abstract val url: String
    abstract val category: Category

    val id: String?
        get() {
            val trimmed = url.trimEnd('/')
            val idStr = trimmed.substringAfterLast('/')
            return idStr.ifEmpty { null }
        }

    abstract val attributes: Map<String, String>

    data class PersonEntity(
        override val name: String,
        val height: String,
        val mass: String,
        val hairColor: String,
        val skinColor: String,
        val eyeColor: String,
        val birthYear: String,
        val gender: String,
        val homeworld: String,
        override val url: String,
        val films: List<String> = emptyList()
    ) : StarWarsEntity() {
        override val category: Category = Category.PEOPLE

        override val attributes: Map<String, String>
            get() = mapOf(
                "Height" to height,
                "Mass" to mass,
                "Hair Color" to hairColor,
                "Skin Color" to skinColor,
                "Eye Color" to eyeColor,
                "Birth Year" to birthYear,
                "Gender" to gender
            )

        fun toPerson(): Person = Person(
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
    }

    data class StarshipEntity(
        override val name: String,
        val model: String,
        val manufacturer: String,
        val costInCredits: String,
        val length: String,
        val maxAtmospheringSpeed: String,
        val crew: String,
        val passengers: String,
        val cargoCapacity: String,
        val consumables: String,
        val hyperdriveRating: String,
        val mglt: String,
        val starshipClass: String,
        override val url: String,
        val pilots: List<String> = emptyList(),
        val films: List<String> = emptyList()
    ) : StarWarsEntity() {
        override val category: Category = Category.STARSHIPS

        override val attributes: Map<String, String>
            get() = mapOf(
                "Model" to model,
                "Manufacturer" to manufacturer,
                "Cost" to costInCredits,
                "Length" to length,
                "Max Speed" to maxAtmospheringSpeed,
                "Crew" to crew,
                "Passengers" to passengers,
                "Cargo Capacity" to cargoCapacity,
                "Class" to starshipClass
            )
    }

    data class PlanetEntity(
        override val name: String,
        val rotationPeriod: String,
        val orbitalPeriod: String,
        val diameter: String,
        val climate: String,
        val gravity: String,
        val terrain: String,
        val surfaceWater: String,
        val population: String,
        override val url: String,
        val residents: List<String> = emptyList(),
        val films: List<String> = emptyList()
    ) : StarWarsEntity() {
        override val category: Category = Category.PLANETS

        override val attributes: Map<String, String>
            get() = mapOf(
                "Rotation Period" to rotationPeriod,
                "Orbital Period" to orbitalPeriod,
                "Diameter" to diameter,
                "Climate" to climate,
                "Gravity" to gravity,
                "Terrain" to terrain,
                "Population" to population
            )
    }

    data class SpeciesEntity(
        override val name: String,
        val classification: String,
        val designation: String,
        val averageHeight: String,
        val skinColors: String,
        val hairColors: String,
        val eyeColors: String,
        val averageLifespan: String,
        val homeworld: String?,
        val language: String,
        override val url: String,
        val people: List<String> = emptyList(),
        val films: List<String> = emptyList()
    ) : StarWarsEntity() {
        override val category: Category = Category.SPECIES

        override val attributes: Map<String, String>
            get() = mapOf(
                "Classification" to classification,
                "Designation" to designation,
                "Avg Height" to averageHeight,
                "Avg Lifespan" to averageLifespan,
                "Language" to language,
                "Skin Colors" to skinColors,
                "Eye Colors" to eyeColors
            )
    }

    data class FilmEntity(
        val title: String,
        val episodeId: Int,
        val openingCrawl: String,
        val director: String,
        val producer: String,
        val releaseDate: String,
        override val url: String,
        val characters: List<String> = emptyList(),
        val planets: List<String> = emptyList(),
        val starships: List<String> = emptyList(),
        val vehicles: List<String> = emptyList(),
        val species: List<String> = emptyList()
    ) : StarWarsEntity() {
        override val name: String get() = title
        override val category: Category = Category.FILMS

        override val attributes: Map<String, String>
            get() = mapOf(
                "Episode" to episodeId.toString(),
                "Director" to director,
                "Producer" to producer,
                "Release Date" to releaseDate
            )
    }
}
