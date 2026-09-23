package com.slashgil.starwars.presentation.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.domain.repository.PersonRepository
import com.slashgil.starwars.domain.usecase.GetPeopleUseCase
import com.slashgil.starwars.domain.usecase.SearchPeopleUseCase
import com.slashgil.starwars.presentation.viewmodel.StarWarsViewModel
import com.slashgil.starwars.ui.theme.StarWarsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3AdaptiveApi::class, ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class StarWarsAppRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val sampleCharacter = Person(
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

    private val fakeRepository = object : PersonRepository {
        override suspend fun getPeople(page: Int): Result<List<Person>> {
            return if (page == 1) Result.success(listOf(sampleCharacter)) else Result.success(emptyList())
        }

        override suspend fun searchPeople(query: String): Result<List<Person>> {
            return Result.success(listOf(sampleCharacter))
        }
    }

    private val getPeopleUseCase = GetPeopleUseCase(fakeRepository)
    private val searchPeopleUseCase = SearchPeopleUseCase(fakeRepository)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @Config(qualifiers = "w1024dp-h768dp")
    fun starWarsApp_tabletMode_showsListAndPlaceholderInitially() {
        val viewModel = StarWarsViewModel(getPeopleUseCase, searchPeopleUseCase)

        composeTestRule.setContent {
            StarWarsTheme {
                StarWarsApp(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText("Luke Skywalker").assertIsDisplayed()
        composeTestRule.onNodeWithText("Select a character to view details").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w1024dp-h768dp")
    fun starWarsApp_tabletMode_clickingCharacterShowsBothPanes() {
        val viewModel = StarWarsViewModel(getPeopleUseCase, searchPeopleUseCase)

        composeTestRule.setContent {
            StarWarsTheme {
                StarWarsApp(viewModel = viewModel)
            }
        }

        // Click character item in list
        composeTestRule.onAllNodesWithText("Luke Skywalker").onFirst().performClick()

        // Verify both character list (on left) and character details (on right) are displayed side-by-side
        // Luke Skywalker appears in both list item and detail top bar
        composeTestRule.onAllNodesWithText("Luke Skywalker").assertCountEquals(2)
        composeTestRule.onNodeWithText("172").assertIsDisplayed() // Height detail in right pane
        composeTestRule.onNodeWithText("Tatooine").assertIsDisplayed() // Homeworld detail in right pane
    }
}
