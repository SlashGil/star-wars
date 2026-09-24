package com.slashgil.starwars.domain.contract

interface SearchEntitiesUseCase {
    suspend operator fun invoke(category: Category, query: String): Result<List<StarWarsEntity>>
}
