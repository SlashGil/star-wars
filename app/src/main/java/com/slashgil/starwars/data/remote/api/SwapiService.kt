package com.slashgil.starwars.data.remote.api

import com.slashgil.starwars.data.remote.model.PersonResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SwapiService {
    @GET("people/")
    suspend fun getPeople(@Query("page") page: Int): PersonResponseDto

    @GET("people/")
    suspend fun searchPeople(@Query("search") query: String): PersonResponseDto
}
