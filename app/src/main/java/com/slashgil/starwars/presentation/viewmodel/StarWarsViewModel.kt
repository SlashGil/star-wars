package com.slashgil.starwars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slashgil.starwars.domain.usecase.GetPeopleUseCase
import com.slashgil.starwars.domain.usecase.SearchPeopleUseCase
import com.slashgil.starwars.presentation.mvi.StarWarsIntent
import com.slashgil.starwars.presentation.mvi.StarWarsUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StarWarsViewModel @Inject constructor(
    private val getPeopleUseCase: GetPeopleUseCase,
    private val searchPeopleUseCase: SearchPeopleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StarWarsUiState())
    val uiState: StateFlow<StarWarsUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        processIntent(StarWarsIntent.LoadHubData)
    }

    fun processIntent(intent: StarWarsIntent) {
        when (intent) {
            is StarWarsIntent.LoadHubData -> loadCharacters()
            is StarWarsIntent.LoadMore -> loadMoreCharacters()
            is StarWarsIntent.Search -> searchCharacters(intent.query)
            is StarWarsIntent.OnCharacterClicked -> selectCharacter(intent.characterUrl)
            is StarWarsIntent.OnBackToHub -> goBackToHub()
        }
    }

    private fun loadCharacters() {
        if (_uiState.value.isLoading) return
        
        _uiState.update { 
            it.copy(
                isLoading = true, 
                error = null, 
                isSearchMode = false, 
                searchQuery = "", 
                currentPage = 1 
            ) 
        }

        viewModelScope.launch {
            getPeopleUseCase(1).fold(
                onSuccess = { people ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hubCharacters = people,
                            currentPage = 1,
                            hasReachedEnd = people.isEmpty()
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "An unexpected error occurred"
                        )
                    }
                }
            )
        }
    }

    private fun loadMoreCharacters() {
        val currentState = _uiState.value
        if (currentState.isLoading || currentState.isLoadingMore || currentState.hasReachedEnd || currentState.isSearchMode) {
            return
        }

        val nextPage = currentState.currentPage + 1
        _uiState.update { it.copy(isLoadingMore = true, error = null) }

        viewModelScope.launch {
            getPeopleUseCase(nextPage).fold(
                onSuccess = { people ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            hubCharacters = it.hubCharacters + people,
                            currentPage = nextPage,
                            hasReachedEnd = people.isEmpty()
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            error = error.message ?: "An unexpected error occurred"
                        )
                    }
                }
            )
        }
    }

    private fun searchCharacters(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        
        searchJob?.cancel()
        
        if (query.isBlank()) {
            loadCharacters()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            _uiState.update { 
                it.copy(
                    isLoading = true, 
                    isSearchMode = true, 
                    error = null 
                ) 
            }
            
            searchPeopleUseCase(query).fold(
                onSuccess = { people ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hubCharacters = people,
                            hasReachedEnd = true // Search results are usually displayed at once without pagination in SWAPI by default or single page
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "An unexpected error occurred"
                        )
                    }
                }
            )
        }
    }

    private fun selectCharacter(url: String) {
        val character = _uiState.value.hubCharacters.find { it.url == url }
        if (character != null) {
            _uiState.update { it.copy(selectedSpokeCharacter = character) }
        }
    }

    private fun goBackToHub() {
        _uiState.update { it.copy(selectedSpokeCharacter = null) }
    }

    class Factory(
        private val getPeopleUseCase: GetPeopleUseCase,
        private val searchPeopleUseCase: SearchPeopleUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StarWarsViewModel::class.java)) {
                return StarWarsViewModel(getPeopleUseCase, searchPeopleUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
