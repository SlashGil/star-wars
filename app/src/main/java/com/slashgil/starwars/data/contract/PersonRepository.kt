package com.slashgil.starwars.data.contract

import com.slashgil.starwars.domain.contract.Person

interface PersonRepository {
    suspend fun getPeople(page: Int): Result<List<Person>>
    suspend fun searchPeople(query: String): Result<List<Person>>
}
