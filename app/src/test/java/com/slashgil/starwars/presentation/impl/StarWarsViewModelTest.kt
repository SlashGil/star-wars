package com.slashgil.starwars.presentation.impl

import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.GetEntitiesUseCase
import com.slashgil.starwars.domain.contract.GetEntityByIdUseCase
import com.slashgil.starwars.domain.contract.GetPeopleUseCase
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.domain.contract.SearchEntitiesUseCase
import com.slashgil.starwars.domain.contract.SearchPeopleUseCase
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.toEntity
import com.slashgil.starwars.presentation.contract.EntityRoute
import com.slashgil.starwars.presentation.contract.StarWarsIntent
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
    private val getEntitiesUseCase: GetEntitiesUseCase = mockk()
    private val searchEntitiesUseCase: SearchEntitiesUseCase = mockk()
    private val getEntityByIdUseCase: GetEntityByIdUseCase = mockk()
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
        url = "https://swapi.info/api/people/1/"
    )

    private val sampleStarship = StarWarsEntity.StarshipEntity(
        name = "Millennium Falcon",
        model = "YT-1300 light freighter",
        manufacturer = "Corellian Engineering Corporation",
        costInCredits = "100000",
        length = "34.37",
        maxAtmospheringSpeed = "1050",
        crew = "4",
        passengers = "6",
        cargoCapacity = "100000",
        consumables = "2 months",
        hyperdriveRating = "0.5",
        mglt = "75",
        starshipClass = "Light freighter",
        url = "https://swapi.info/api/starships/10/"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getPeopleUseCase(1) } returns Result.success(listOf(samplePerson))
        coEvery { getEntitiesUseCase(Category.STARSHIPS, 1) } returns Result.success(listOf(sampleStarship))
        viewModel = StarWarsViewModel(
            getEntitiesUseCase = getEntitiesUseCase,
            searchEntitiesUseCase = searchEntitiesUseCase,
            getPeopleUseCase = getPeopleUseCase,
            searchPeopleUseCase = searchPeopleUseCase,
            getEntityByIdUseCase = getEntityByIdUseCase
        )
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
        assertEquals(Category.PEOPLE, state.selectedCategory)
    }

    @Test
    fun `LoadMore appends characters successfully`() = runTest {
        val page2Person = samplePerson.copy(name = "C-3PO", url = "https://swapi.info/api/people/2/")
        coEvery { getPeopleUseCase(2) } returns Result.success(listOf(page2Person))

        viewModel.processIntent(StarWarsIntent.LoadMore)

        val state = viewModel.uiState.value
        assertEquals(2, state.hubCharacters.size)
        assertEquals(2, state.currentPage)
    }

    @Test
    fun `SelectCategory updates selected category and loads entities`() = runTest {
        viewModel.processIntent(StarWarsIntent.SelectCategory(Category.STARSHIPS))

        val state = viewModel.uiState.value
        assertEquals(Category.STARSHIPS, state.selectedCategory)
        assertEquals(1, state.hubEntities.size)
        assertEquals(sampleStarship, state.hubEntities.first())
        assertNull(state.selectedEntity)
    }

    @Test
    fun `OnCharacterClicked selects character and sets navigationStack`() {
        viewModel.processIntent(StarWarsIntent.OnCharacterClicked(samplePerson.url))

        val state = viewModel.uiState.value
        assertEquals(samplePerson, state.selectedSpokeCharacter)
        assertEquals(1, state.navigationStack.size)
        assertEquals(EntityRoute(Category.PEOPLE, "1"), state.navigationStack.first())
    }

    @Test
    fun `OnEntityClicked selects entity and sets navigationStack`() = runTest {
        viewModel.processIntent(StarWarsIntent.SelectCategory(Category.STARSHIPS))
        viewModel.processIntent(StarWarsIntent.OnEntityClicked(sampleStarship.url))

        val state = viewModel.uiState.value
        assertEquals(sampleStarship, state.selectedEntity)
        assertEquals(1, state.navigationStack.size)
        assertEquals(EntityRoute(Category.STARSHIPS, "10"), state.navigationStack.first())
    }

    @Test
    fun `OnBackToHub clears selected character, entity, and navigationStack`() {
        viewModel.processIntent(StarWarsIntent.OnCharacterClicked(samplePerson.url))
        viewModel.processIntent(StarWarsIntent.OnBackToHub)

        val state = viewModel.uiState.value
        assertNull(state.selectedSpokeCharacter)
        assertNull(state.selectedEntity)
        assertTrue(state.navigationStack.isEmpty())
    }

    @Test
    fun `NavigateToRelatedEntity pushes route and fetches entity`() = runTest {
        val filmEntity = StarWarsEntity.FilmEntity(
            title = "A New Hope",
            episodeId = 4,
            openingCrawl = "It is a period of civil war...",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            url = "https://swapi.info/api/films/1/"
        )
        coEvery { getEntityByIdUseCase(Category.FILMS, "1") } returns Result.success(filmEntity)

        viewModel.processIntent(StarWarsIntent.NavigateToRelatedEntity(Category.FILMS, "1"))

        val state = viewModel.uiState.value
        assertEquals(1, state.navigationStack.size)
        assertEquals(EntityRoute(Category.FILMS, "1"), state.navigationStack.first())
        assertEquals(filmEntity, state.selectedEntity)
    }

    @Test
    fun `OnNavigateBack pops route and restores previous entity`() = runTest {
        val filmEntity = StarWarsEntity.FilmEntity(
            title = "A New Hope",
            episodeId = 4,
            openingCrawl = "It is a period of civil war...",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            url = "https://swapi.info/api/films/1/"
        )
        val personEntity = samplePerson.toEntity()

        coEvery { getEntityByIdUseCase(Category.FILMS, "1") } returns Result.success(filmEntity)
        coEvery { getEntityByIdUseCase(Category.PEOPLE, "1") } returns Result.success(personEntity)

        // Push route 1
        viewModel.processIntent(StarWarsIntent.NavigateToRelatedEntity(Category.PEOPLE, "1"))
        // Push route 2
        viewModel.processIntent(StarWarsIntent.NavigateToRelatedEntity(Category.FILMS, "1"))

        assertEquals(2, viewModel.uiState.value.navigationStack.size)
        assertEquals(filmEntity, viewModel.uiState.value.selectedEntity)

        // Pop route 2 -> back to route 1
        viewModel.processIntent(StarWarsIntent.OnNavigateBack)

        val state1 = viewModel.uiState.value
        assertEquals(1, state1.navigationStack.size)
        assertEquals(EntityRoute(Category.PEOPLE, "1"), state1.navigationStack.last())
        assertEquals(personEntity, state1.selectedEntity)

        // Pop route 1 -> back to hub (empty stack)
        viewModel.processIntent(StarWarsIntent.OnNavigateBack)

        val state2 = viewModel.uiState.value
        assertTrue(state2.navigationStack.isEmpty())
        assertNull(state2.selectedEntity)
    }
}
