package com.slashgil.starwars.presentation.contract

import com.slashgil.starwars.domain.contract.Category

sealed class StarWarsIntent {
    object LoadHubData : StarWarsIntent()
    object LoadMore : StarWarsIntent()
    data class Search(val query: String) : StarWarsIntent()
    data class OnCharacterClicked(val characterUrl: String) : StarWarsIntent()
    data class OnEntityClicked(val entityUrl: String) : StarWarsIntent()
    data class SelectCategory(val category: Category) : StarWarsIntent()
    object OnBackToHub : StarWarsIntent()
    data class NavigateToRelatedEntity(val category: Category, val id: String) : StarWarsIntent()
    object OnNavigateBack : StarWarsIntent()
}
