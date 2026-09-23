package com.slashgil.starwars.presentation.viewmodel

import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.domain.usecase.GetPeopleUseCase
import com.slashgil.starwars.domain.usecase.SearchPeopleUseCase
import com.slashgil.starwars.presentation.mvi.StarWarsIntent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StarWarsViewModelTest {

    private val getPeopleUseCase: GetPeopleUseCase = mockk()
    private val searchPeopleUseCase: SearchPeopleUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: StarWarsViewModel

    private val samplePerson = Person(
        name = "Luke Skywalker",
        height = "172",
        mass = "77",
        hairColor = "blond",
        skinColor = "fair",
        eyeColor = "blue",
        birthYear = "19BBY",
        gender = "male",
        homeworld = "Tatooine",
        url = "https://swapi.dev/api/people/1/"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getPeopleUseCase(1) } returns Result.success(listOf(samplePerson))
        viewModel = StarWarsViewModel(getPeopleUseCase, searchPeopleUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization loads hub characters`() {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.hubCharacters.size)
        assertEquals(samplePerson, state.hubCharacters.first())
    }

    @Test
    fun `LoadMore appends characters successfully`() = runTest {
        val page2Person = samplePerson.copy(name = "C-3PO", url = "https://swapi.dev/api/people/2/")
        coEvery { getPeopleUseCase(2) } returns Result.success(listOf(page2Person))

        viewModel.processIntent(StarWarsIntent.LoadMore)

        val state = viewModel.uiState.value
        assertEquals(2, state.hubCharacters.size)
        assertEquals(2, state.currentPage)
    }

    @Test
    fun `OnCharacterClicked selects character`() {
        viewModel.processIntent(StarWarsIntent.OnCharacterClicked(samplePerson.url))

        val state = viewModel.uiState.value
        assertEquals(samplePerson, state.selectedSpokeCharacter)
    }

    @Test
    fun `OnBackToHub clears selected character`() {
        viewModel.processIntent(StarWarsIntent.OnCharacterClicked(samplePerson.url))
        viewModel.processIntent(StarWarsIntent.OnBackToHub)

        val state = viewModel.uiState.value
        assertNull(state.selectedSpokeCharacter)
    }
}
