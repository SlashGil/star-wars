package com.slashgil.starwars.presentation.contract

import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.toEntity

data class EntityRoute(
    val category: Category,
    val id: String
)

data class StarWarsUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hubEntities: List<StarWarsEntity> = emptyList(),
    val hubCharacters: List<Person> = emptyList(),
    val selectedCategory: Category = Category.PEOPLE,
    val selectedEntity: StarWarsEntity? = null,
    val selectedSpokeCharacter: Person? = null,
    val error: String? = null,
    val currentPage: Int = 1,
    val searchQuery: String = "",
    val isSearchMode: Boolean = false,
    val hasReachedEnd: Boolean = false,
    val navigationStack: List<EntityRoute> = emptyList(),
) {
    val displayEntities: List<StarWarsEntity>
        get() = hubEntities.ifEmpty { hubCharacters.map { it.toEntity() } }

    val displaySelectedEntity: StarWarsEntity?
        get() = selectedEntity ?: selectedSpokeCharacter?.toEntity()
}
