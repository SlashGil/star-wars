package com.slashgil.starwars.presentation.contract

import com.slashgil.starwars.domain.contract.AppFont
import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.ForceAllegiance
import com.slashgil.starwars.domain.contract.LightsaberColor

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
    object OpenSettings : StarWarsIntent()
    object CloseSettings : StarWarsIntent()
    data class SetAllegiance(val allegiance: ForceAllegiance) : StarWarsIntent()
    data class SetLightsaberColor(val color: LightsaberColor) : StarWarsIntent()
    data class SetAppFont(val font: AppFont) : StarWarsIntent()
}
