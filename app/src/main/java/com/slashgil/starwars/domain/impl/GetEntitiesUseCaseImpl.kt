package com.slashgil.starwars.domain.impl

import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.GetEntitiesUseCase
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.StarWarsRepository
import javax.inject.Inject

class GetEntitiesUseCaseImpl @Inject constructor(
    private val repository: StarWarsRepository
) : GetEntitiesUseCase {
    override suspend operator fun invoke(category: Category, page: Int): Result<List<StarWarsEntity>> {
        return repository.getEntities(category, page)
    }
}
