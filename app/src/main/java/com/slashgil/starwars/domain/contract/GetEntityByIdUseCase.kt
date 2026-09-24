package com.slashgil.starwars.domain.contract

interface GetEntityByIdUseCase {
    suspend operator fun invoke(category: Category, id: String): Result<StarWarsEntity>
}
