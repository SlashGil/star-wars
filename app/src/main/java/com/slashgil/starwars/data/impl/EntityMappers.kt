package com.slashgil.starwars.data.impl

import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.domain.contract.StarWarsEntity

fun PersonDto.toDomain(): Person {
    return Person(
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

fun PersonDto.toEntity(): StarWarsEntity.PersonEntity {
    return StarWarsEntity.PersonEntity(
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

fun StarshipDto.toEntity(): StarWarsEntity.StarshipEntity {
    return StarWarsEntity.StarshipEntity(
        name = name,
        model = model,
        manufacturer = manufacturer,
        costInCredits = costInCredits,
        length = length,
        maxAtmospheringSpeed = maxAtmospheringSpeed,
        crew = crew,
        passengers = passengers,
        cargoCapacity = cargoCapacity,
        consumables = consumables,
        hyperdriveRating = hyperdriveRating,
        mglt = mglt,
        starshipClass = starshipClass,
        url = url,
        pilots = pilots,
        films = films
    )
}

fun PlanetDto.toEntity(): StarWarsEntity.PlanetEntity {
    return StarWarsEntity.PlanetEntity(
        name = name,
        rotationPeriod = rotationPeriod,
        orbitalPeriod = orbitalPeriod,
        diameter = diameter,
        climate = climate,
        gravity = gravity,
        terrain = terrain,
        surfaceWater = surfaceWater,
        population = population,
        url = url,
        residents = residents,
        films = films
    )
}

fun SpeciesDto.toEntity(): StarWarsEntity.SpeciesEntity {
    return StarWarsEntity.SpeciesEntity(
        name = name,
        classification = classification,
        designation = designation,
        averageHeight = averageHeight,
        skinColors = skinColors,
        hairColors = hairColors,
        eyeColors = eyeColors,
        averageLifespan = averageLifespan,
        homeworld = homeworld,
        language = language,
        url = url,
        people = people,
        films = films
    )
}

fun FilmDto.toEntity(): StarWarsEntity.FilmEntity {
    return StarWarsEntity.FilmEntity(
        title = title,
        episodeId = episodeId,
        openingCrawl = openingCrawl,
        director = director,
        producer = producer,
        releaseDate = releaseDate,
        url = url,
        characters = characters,
        planets = planets,
        starships = starships,
        vehicles = vehicles,
        species = species
    )
}
