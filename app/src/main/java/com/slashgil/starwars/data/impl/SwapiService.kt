package com.slashgil.starwars.data.impl

import retrofit2.http.GET
import retrofit2.http.Path

interface SwapiService {
    @GET("people")
    suspend fun getPeople(): List<PersonDto>

    @GET("people/{id}")
    suspend fun getPerson(@Path("id") id: String): PersonDto

    @GET("starships")
    suspend fun getStarships(): List<StarshipDto>

    @GET("starships/{id}")
    suspend fun getStarship(@Path("id") id: String): StarshipDto

    @GET("planets")
    suspend fun getPlanets(): List<PlanetDto>

    @GET("planets/{id}")
    suspend fun getPlanet(@Path("id") id: String): PlanetDto

    @GET("species")
    suspend fun getSpecies(): List<SpeciesDto>

    @GET("species/{id}")
    suspend fun getSpeciesItem(@Path("id") id: String): SpeciesDto

    @GET("films")
    suspend fun getFilms(): List<FilmDto>

    @GET("films/{id}")
    suspend fun getFilm(@Path("id") id: String): FilmDto
}
