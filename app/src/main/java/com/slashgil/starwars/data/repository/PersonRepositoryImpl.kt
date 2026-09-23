package com.slashgil.starwars.data.repository

import com.slashgil.starwars.data.mapper.toDomain
import com.slashgil.starwars.data.remote.api.SwapiService
import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.domain.repository.PersonRepository

import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val api: SwapiService
) : PersonRepository {

    override suspend fun getPeople(page: Int): Result<List<Person>> {
        return try {
            val response = api.getPeople(page)
            Result.success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchPeople(query: String): Result<List<Person>> {
        return try {
            val response = api.searchPeople(query)
            Result.success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
