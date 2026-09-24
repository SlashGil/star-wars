package com.slashgil.starwars.domain.contract

interface GetEntitiesUseCase {
    suspend operator fun invoke(category: Category, page: Int = 1): Result<List<StarWarsEntity>>
}
