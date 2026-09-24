package com.slashgil.starwars.presentation.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.GetEntitiesUseCase
import com.slashgil.starwars.domain.contract.GetEntityByIdUseCase
import com.slashgil.starwars.domain.contract.GetPeopleUseCase
import com.slashgil.starwars.domain.contract.SearchEntitiesUseCase
import com.slashgil.starwars.domain.contract.SearchPeopleUseCase
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.StarWarsRepository
import com.slashgil.starwars.domain.contract.toEntity
import com.slashgil.starwars.presentation.contract.EntityRoute
import com.slashgil.starwars.presentation.contract.StarWarsIntent
import com.slashgil.starwars.presentation.contract.StarWarsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarWarsViewModel @Inject constructor(
    private val getEntitiesUseCase: GetEntitiesUseCase,
    private val searchEntitiesUseCase: SearchEntitiesUseCase,
    private val getPeopleUseCase: GetPeopleUseCase? = null,
    private val searchPeopleUseCase: SearchPeopleUseCase? = null,
    private val getEntityByIdUseCase: GetEntityByIdUseCase? = null,
    private val repository: StarWarsRepository? = null
) : ViewModel() {

    constructor(
        getPeopleUseCase: GetPeopleUseCase,
        searchPeopleUseCase: SearchPeopleUseCase,
        getEntityByIdUseCase: GetEntityByIdUseCase? = null
    ) : this(
        getEntitiesUseCase = object : GetEntitiesUseCase {
            override suspend fun invoke(category: Category, page: Int): Result<List<StarWarsEntity>> {
                return if (category == Category.PEOPLE) {
                    getPeopleUseCase(page).map { people -> people.map { it.toEntity() } }
                } else {
                    Result.success(emptyList())
                }
            }
        },
        searchEntitiesUseCase = object : SearchEntitiesUseCase {
            override suspend fun invoke(category: Category, query: String): Result<List<StarWarsEntity>> {
                return if (category == Category.PEOPLE) {
                    searchPeopleUseCase(query).map { people -> people.map { it.toEntity() } }
                } else {
                    Result.success(emptyList())
                }
            }
        },
        getPeopleUseCase = getPeopleUseCase,
        searchPeopleUseCase = searchPeopleUseCase,
        getEntityByIdUseCase = getEntityByIdUseCase
    )

    private val _uiState = MutableStateFlow(StarWarsUiState())
    val uiState: StateFlow<StarWarsUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        processIntent(StarWarsIntent.LoadHubData)
    }

    fun processIntent(intent: StarWarsIntent) {
        when (intent) {
            is StarWarsIntent.LoadHubData -> loadEntities()
            is StarWarsIntent.LoadMore -> loadMoreEntities()
            is StarWarsIntent.Search -> searchEntities(intent.query)
            is StarWarsIntent.SelectCategory -> selectCategory(intent.category)
            is StarWarsIntent.OnCharacterClicked -> selectEntity(intent.characterUrl)
            is StarWarsIntent.OnEntityClicked -> selectEntity(intent.entityUrl)
            is StarWarsIntent.OnBackToHub -> goBackToHub()
            is StarWarsIntent.NavigateToRelatedEntity -> navigateToRelatedEntity(intent.category, intent.id)
            is StarWarsIntent.OnNavigateBack -> navigateBack()
        }
    }

    private fun selectCategory(category: Category) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                selectedCategory = category,
                selectedEntity = null,
                selectedSpokeCharacter = null,
                navigationStack = emptyList(),
                searchQuery = "",
                isSearchMode = false,
                hubEntities = emptyList(),
                hubCharacters = emptyList(),
                currentPage = 1,
                hasReachedEnd = false,
                error = null
            )
        }
        loadEntities(category)
    }

    private fun loadEntities(category: Category = _uiState.value.selectedCategory) {
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isSearchMode = false,
                searchQuery = "",
                currentPage = 1,
                selectedCategory = category
            )
        }

        viewModelScope.launch {
            val result = if (category == Category.PEOPLE && getPeopleUseCase != null) {
                getPeopleUseCase.invoke(1).map { people -> people.map { it.toEntity() } }
            } else {
                getEntitiesUseCase(category, 1)
            }

            result.fold(
                onSuccess = { entities ->
                    val people = entities.filterIsInstance<StarWarsEntity.PersonEntity>().map { it.toPerson() }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hubEntities = entities,
                            hubCharacters = people,
                            currentPage = 1,
                            hasReachedEnd = entities.isEmpty()
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

    private fun loadMoreEntities() {
        val currentState = _uiState.value
        if (currentState.isLoading || currentState.isLoadingMore || currentState.hasReachedEnd || currentState.isSearchMode) {
            return
        }

        val nextPage = currentState.currentPage + 1
        val category = currentState.selectedCategory
        _uiState.update { it.copy(isLoadingMore = true, error = null) }

        viewModelScope.launch {
            val result = if (category == Category.PEOPLE && getPeopleUseCase != null) {
                getPeopleUseCase.invoke(nextPage).map { people -> people.map { it.toEntity() } }
            } else {
                getEntitiesUseCase(category, nextPage)
            }

            result.fold(
                onSuccess = { newEntities ->
                    val newPeople = newEntities.filterIsInstance<StarWarsEntity.PersonEntity>().map { it.toPerson() }
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            hubEntities = it.hubEntities + newEntities,
                            hubCharacters = it.hubCharacters + newPeople,
                            currentPage = nextPage,
                            hasReachedEnd = newEntities.isEmpty()
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

    private fun searchEntities(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        searchJob?.cancel()

        if (query.isBlank()) {
            loadEntities(_uiState.value.selectedCategory)
            return
        }

        val category = _uiState.value.selectedCategory

        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isSearchMode = true,
                    error = null
                )
            }

            val result = if (category == Category.PEOPLE && searchPeopleUseCase != null) {
                searchPeopleUseCase.invoke(query).map { people -> people.map { it.toEntity() } }
            } else {
                searchEntitiesUseCase(category, query)
            }

            result.fold(
                onSuccess = { entities ->
                    val people = entities.filterIsInstance<StarWarsEntity.PersonEntity>().map { it.toPerson() }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hubEntities = entities,
                            hubCharacters = people,
                            hasReachedEnd = true
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

    private fun selectEntity(url: String) {
        val currentState = _uiState.value
        val entity = currentState.displayEntities.find { it.url == url }
        val character = currentState.hubCharacters.find { it.url == url }
            ?: (entity as? StarWarsEntity.PersonEntity)?.toPerson()

        val selected = entity ?: character?.toEntity()
        if (selected != null) {
            val route = selected.id?.let { EntityRoute(selected.category, it) }
            val newStack = if (route != null) listOf(route) else emptyList()
            _uiState.update {
                it.copy(
                    selectedEntity = selected,
                    selectedSpokeCharacter = character ?: (selected as? StarWarsEntity.PersonEntity)?.toPerson(),
                    navigationStack = newStack
                )
            }
        }
    }

    private fun navigateToRelatedEntity(category: Category, id: String) {
        val route = EntityRoute(category, id)
        _uiState.update {
            it.copy(
                navigationStack = it.navigationStack + route
            )
        }
        loadEntityDetails(category, id)
    }

    private fun navigateBack() {
        val currentStack = _uiState.value.navigationStack
        if (currentStack.isEmpty()) {
            _uiState.update {
                it.copy(
                    selectedEntity = null,
                    selectedSpokeCharacter = null
                )
            }
            return
        }

        val newStack = currentStack.dropLast(1)
        if (newStack.isEmpty()) {
            _uiState.update {
                it.copy(
                    navigationStack = emptyList(),
                    selectedEntity = null,
                    selectedSpokeCharacter = null
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    navigationStack = newStack
                )
            }
            val topRoute = newStack.last()
            loadEntityDetails(topRoute.category, topRoute.id)
        }
    }

    private fun loadEntityDetails(category: Category, id: String) {
        val existing = _uiState.value.displayEntities.find { it.category == category && it.id == id }
        if (existing != null) {
            _uiState.update {
                it.copy(
                    selectedEntity = existing,
                    selectedSpokeCharacter = (existing as? StarWarsEntity.PersonEntity)?.toPerson()
                )
            }
        } else {
            _uiState.update { it.copy(isLoading = true, error = null) }
        }

        viewModelScope.launch {
            val result = getEntityByIdUseCase?.invoke(category, id)
                ?: repository?.getEntityById(category, id)
                ?: _uiState.value.displayEntities.find { it.category == category && it.id == id }?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("Entity loader unavailable"))

            result.fold(
                onSuccess = { entity ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            selectedEntity = entity,
                            selectedSpokeCharacter = (entity as? StarWarsEntity.PersonEntity)?.toPerson()
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load entity"
                        )
                    }
                }
            )
        }
    }

    private fun goBackToHub() {
        _uiState.update {
            it.copy(
                selectedEntity = null,
                selectedSpokeCharacter = null,
                navigationStack = emptyList()
            )
        }
    }

    class Factory(
        private val getEntitiesUseCase: GetEntitiesUseCase,
        private val searchEntitiesUseCase: SearchEntitiesUseCase,
        private val getPeopleUseCase: GetPeopleUseCase? = null,
        private val searchPeopleUseCase: SearchPeopleUseCase? = null,
        private val getEntityByIdUseCase: GetEntityByIdUseCase? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StarWarsViewModel::class.java)) {
                return StarWarsViewModel(
                    getEntitiesUseCase = getEntitiesUseCase,
                    searchEntitiesUseCase = searchEntitiesUseCase,
                    getPeopleUseCase = getPeopleUseCase,
                    searchPeopleUseCase = searchPeopleUseCase,
                    getEntityByIdUseCase = getEntityByIdUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
