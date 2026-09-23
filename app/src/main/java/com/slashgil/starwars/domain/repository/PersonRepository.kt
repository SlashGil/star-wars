package com.slashgil.starwars.domain.repository

import com.slashgil.starwars.domain.model.Person

interface PersonRepository {
    suspend fun getPeople(page: Int): Result<List<Person>>
    suspend fun searchPeople(query: String): Result<List<Person>>
}
