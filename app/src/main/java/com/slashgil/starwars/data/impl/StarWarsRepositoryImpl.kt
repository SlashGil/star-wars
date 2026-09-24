package com.slashgil.starwars.data.impl

import com.slashgil.starwars.data.contract.PersonRepository
import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.StarWarsRepository
import javax.inject.Inject

class StarWarsRepositoryImpl @Inject constructor(
    private val api: SwapiService
) : StarWarsRepository, PersonRepository {

    override suspend fun getEntities(category: Category, page: Int): Result<List<StarWarsEntity>> {
        return try {
            val allEntities = fetchAllEntities(category)
            val pageSize = 10
            val startIndex = (page - 1) * pageSize
            if (startIndex < 0 || startIndex >= allEntities.size) {
                Result.success(emptyList())
            } else {
                val endIndex = minOf(startIndex + pageSize, allEntities.size)
                Result.success(allEntities.subList(startIndex, endIndex))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchEntities(category: Category, query: String): Result<List<StarWarsEntity>> {
        return try {
            val allEntities = fetchAllEntities(category)
            val filtered = if (query.isBlank()) {
                allEntities
            } else {
                allEntities.filter { it.name.contains(query, ignoreCase = true) }
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEntityById(category: Category, id: String): Result<StarWarsEntity> {
        return try {
            val entity: StarWarsEntity = when (category) {
                Category.PEOPLE -> api.getPerson(id).toEntity()
                Category.STARSHIPS -> api.getStarship(id).toEntity()
                Category.PLANETS -> api.getPlanet(id).toEntity()
                Category.SPECIES -> api.getSpeciesItem(id).toEntity()
                Category.FILMS -> api.getFilm(id).toEntity()
            }
            Result.success(entity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPeople(page: Int): Result<List<Person>> {
        return getEntities(Category.PEOPLE, page).map { entities ->
            entities.filterIsInstance<StarWarsEntity.PersonEntity>().map { it.toPerson() }
        }
    }

    override suspend fun searchPeople(query: String): Result<List<Person>> {
        return searchEntities(Category.PEOPLE, query).map { entities ->
            entities.filterIsInstance<StarWarsEntity.PersonEntity>().map { it.toPerson() }
        }
    }

    private suspend fun fetchAllEntities(category: Category): List<StarWarsEntity> {
        return when (category) {
            Category.PEOPLE -> api.getPeople().map { it.toEntity() }
            Category.STARSHIPS -> api.getStarships().map { it.toEntity() }
            Category.PLANETS -> api.getPlanets().map { it.toEntity() }
            Category.SPECIES -> api.getSpecies().map { it.toEntity() }
            Category.FILMS -> api.getFilms().map { it.toEntity() }
        }
    }
}

typealias PersonRepositoryImpl = StarWarsRepositoryImpl
