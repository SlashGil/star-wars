package com.slashgil.starwars.presentation.mvi

import com.slashgil.starwars.domain.model.Person

data class StarWarsUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hubCharacters: List<Person> = emptyList(),
    val selectedSpokeCharacter: Person? = null,
    val error: String? = null,
    val currentPage: Int = 1,
    val searchQuery: String = "",
    val isSearchMode: Boolean = false,
    val hasReachedEnd: Boolean = false
)
