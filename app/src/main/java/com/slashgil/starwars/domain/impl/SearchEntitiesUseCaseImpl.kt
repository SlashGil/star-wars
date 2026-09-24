package com.slashgil.starwars.domain.impl

import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.SearchEntitiesUseCase
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.StarWarsRepository
import javax.inject.Inject

class SearchEntitiesUseCaseImpl @Inject constructor(
    private val repository: StarWarsRepository
) : SearchEntitiesUseCase {
    override suspend operator fun invoke(category: Category, query: String): Result<List<StarWarsEntity>> {
        return repository.searchEntities(category, query)
    }
}
