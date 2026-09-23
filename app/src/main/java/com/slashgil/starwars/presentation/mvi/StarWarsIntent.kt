package com.slashgil.starwars.presentation.mvi

sealed class StarWarsIntent {
    object LoadHubData : StarWarsIntent()
    object LoadMore : StarWarsIntent()
    data class Search(val query: String) : StarWarsIntent()
    data class OnCharacterClicked(val characterUrl: String) : StarWarsIntent()
    object OnBackToHub : StarWarsIntent()
}
