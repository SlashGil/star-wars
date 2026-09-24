package com.slashgil.starwars.presentation.contract

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import coil.Coil
import coil.ImageLoader
import coil.decode.DataSource
import coil.intercept.Interceptor
import coil.request.SuccessResult
import com.slashgil.starwars.data.contract.PersonRepository
import com.slashgil.starwars.domain.contract.GetPeopleUseCase
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.domain.contract.SearchPeopleUseCase
import com.slashgil.starwars.domain.impl.GetPeopleUseCaseImpl
import com.slashgil.starwars.domain.impl.SearchPeopleUseCaseImpl
import com.slashgil.starwars.presentation.impl.StarWarsViewModel
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
        url = "https://swapi.info/api/people/1/"
    )

    private val fakeRepository = object : PersonRepository {
        override suspend fun getPeople(page: Int): Result<List<Person>> {
            return if (page == 1) Result.success(listOf(sampleCharacter)) else Result.success(emptyList())
        }

        override suspend fun searchPeople(query: String): Result<List<Person>> {
            return Result.success(listOf(sampleCharacter))
        }
    }

    private val getPeopleUseCase: GetPeopleUseCase = GetPeopleUseCaseImpl(fakeRepository)
    private val searchPeopleUseCase: SearchPeopleUseCase = SearchPeopleUseCaseImpl(fakeRepository)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add { chain ->
                    SuccessResult(
                        drawable = ColorDrawable(Color.BLACK),
                        request = chain.request,
                        dataSource = DataSource.MEMORY_CACHE
                    )
                }
            }
            .build()
        Coil.setImageLoader(imageLoader)
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
        composeTestRule.onNodeWithText("172").performScrollTo().assertIsDisplayed() // Height detail in right pane
        composeTestRule.onNodeWithText("Tatooine").performScrollTo().assertIsDisplayed() // Homeworld detail in right pane
    }
}
