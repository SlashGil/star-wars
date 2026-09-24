package com.slashgil.starwars.domain.impl

import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.GetEntityByIdUseCase
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.StarWarsRepository
import javax.inject.Inject

class GetEntityByIdUseCaseImpl @Inject constructor(
    private val repository: StarWarsRepository
) : GetEntityByIdUseCase {
    override suspend operator fun invoke(category: Category, id: String): Result<StarWarsEntity> {
        return repository.getEntityById(category, id)
    }
}
