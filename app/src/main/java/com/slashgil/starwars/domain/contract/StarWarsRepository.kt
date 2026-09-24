package com.slashgil.starwars.domain.contract

interface StarWarsRepository {
    suspend fun getEntities(category: Category, page: Int = 1): Result<List<StarWarsEntity>>
    suspend fun searchEntities(category: Category, query: String): Result<List<StarWarsEntity>>
    suspend fun getEntityById(category: Category, id: String): Result<StarWarsEntity>
}
