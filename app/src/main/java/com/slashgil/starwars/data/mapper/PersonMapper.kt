package com.slashgil.starwars.data.mapper

import com.slashgil.starwars.data.remote.model.PersonDto
import com.slashgil.starwars.domain.model.Person

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
