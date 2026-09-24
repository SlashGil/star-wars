package com.slashgil.starwars.domain.contract

interface CharacterImageResolver {
    fun resolveCharacterImageUrl(characterUrl: String): String
    fun resolveCharacterName(characterUrl: String): String
    fun resolveFilm(filmUrl: String): Film
    fun resolveFilmImageUrl(filmUrl: String): String
    fun resolveFilmTitle(filmUrl: String): String
    fun resolvePlanetName(planetUrl: String): String
    fun resolveSpeciesName(speciesUrl: String): String
    fun resolveStarshipName(starshipUrl: String): String
    fun resolveImageUrl(category: Category, url: String): String
    fun resolveEntityImageUrl(entity: StarWarsEntity): String
}

typealias StarWarsImageResolver = CharacterImageResolver
