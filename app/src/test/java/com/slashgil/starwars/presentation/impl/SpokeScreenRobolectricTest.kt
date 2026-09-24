package com.slashgil.starwars.presentation.impl

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import coil.Coil
import coil.ImageLoader
import coil.decode.DataSource
import coil.request.SuccessResult
import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.ui.theme.StarWarsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w400dp-h1024dp")
class SpokeScreenRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDispatcher = UnconfinedTestDispatcher()

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

    private val sampleCharacter = Person(
        name = "Darth Vader",
        height = "202",
        mass = "136",
        hairColor = "none",
        skinColor = "white",
        eyeColor = "yellow",
        birthYear = "41.9BBY",
        gender = "male",
        homeworld = "Tatooine",
        url = "https://swapi.info/api/people/4/"
    )

    @Test
    fun spokeScreen_displaysCharacterDetails() {
        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    character = sampleCharacter,
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Darth Vader").assertIsDisplayed()
        composeTestRule.onNodeWithText("202").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("136").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("41.9BBY").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun filmCard_clickingCard_invokesOnClick() {
        var clicked = false
        composeTestRule.setContent {
            StarWarsTheme {
                FilmCard(
                    filmUrl = "https://swapi.info/api/films/1/",
                    onClick = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Episode IV: A New Hope").performClick()
        assertTrue(clicked)
    }

    @Test
    fun characterCard_clickingCard_invokesOnClick() {
        var clicked = false
        composeTestRule.setContent {
            StarWarsTheme {
                CharacterCard(
                    characterUrl = "https://swapi.info/api/people/1/",
                    onClick = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Luke Skywalker").performClick()
        assertTrue(clicked)
    }

    @Test
    fun spokeScreen_clickingBackButton_triggersOnBack() {
        var backClicked = false

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    character = sampleCharacter,
                    onBack = { backClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Back to Hub").performClick()

        assertTrue(backClicked)
    }

    @Test
    fun spokeScreen_filmDetail_displaysResolvedCharacterNames() {
        val filmEntity = StarWarsEntity.FilmEntity(
            title = "A New Hope",
            episodeId = 4,
            openingCrawl = "It is a period of civil war...",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            url = "https://swapi.info/api/films/1/",
            characters = listOf(
                "https://swapi.info/api/people/1/",
                "https://swapi.info/api/people/2/"
            )
        )

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    entity = filmEntity,
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("A New Hope").assertIsDisplayed()
        composeTestRule.onNodeWithText("Luke Skywalker").assertExists()
        composeTestRule.onNodeWithText("C-3PO").assertExists()
    }

    @Test
    fun spokeScreen_clickingBackButton_triggersOnNavigateBack() {
        var backClicked = false

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    character = sampleCharacter,
                    onNavigateBack = { backClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Back to Hub").performClick()

        assertTrue(backClicked)
    }

    @Test
    fun spokeScreen_clickingFilmCard_triggersOnNavigateToRelated() {
        var clickedCategory: Category? = null
        var clickedId: String? = null

        val personEntity = StarWarsEntity.PersonEntity(
            name = "Luke Skywalker",
            height = "172",
            mass = "77",
            hairColor = "blond",
            skinColor = "fair",
            eyeColor = "blue",
            birthYear = "19BBY",
            gender = "male",
            homeworld = "https://swapi.info/api/planets/1/",
            url = "https://swapi.info/api/people/1/",
            films = listOf("https://swapi.info/api/films/1/")
        )

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    entity = personEntity,
                    onNavigateToRelated = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    },
                    onNavigateToRelatedEntity = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Film Appearances").performScrollTo()
        composeTestRule.onNodeWithTag("related_card_FILMS_1").performClick()

        assertEquals(Category.FILMS, clickedCategory)
        assertEquals("1", clickedId)
    }

    @Test
    fun spokeScreen_clickingCharacterCard_triggersOnNavigateToRelated() {
        var clickedCategory: Category? = null
        var clickedId: String? = null

        val filmEntity = StarWarsEntity.FilmEntity(
            title = "A New Hope",
            episodeId = 4,
            openingCrawl = "It is a period of civil war...",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            url = "https://swapi.info/api/films/1/",
            characters = listOf("https://swapi.info/api/people/1/")
        )

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    entity = filmEntity,
                    onNavigateToRelated = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    },
                    onNavigateToRelatedEntity = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Characters").performScrollTo()
        composeTestRule.onNodeWithTag("related_card_PEOPLE_1").performClick()

        assertEquals(Category.PEOPLE, clickedCategory)
        assertEquals("1", clickedId)
    }

    @Test
    fun spokeScreen_clickingPlanetCard_triggersOnNavigateToRelated() {
        var clickedCategory: Category? = null
        var clickedId: String? = null

        val filmEntity = StarWarsEntity.FilmEntity(
            title = "A New Hope",
            episodeId = 4,
            openingCrawl = "It is a period of civil war...",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            url = "https://swapi.info/api/films/1/",
            planets = listOf("https://swapi.info/api/planets/1/")
        )

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    entity = filmEntity,
                    onNavigateToRelated = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    },
                    onNavigateToRelatedEntity = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Planets").performScrollTo()
        composeTestRule.onNodeWithTag("related_card_PLANETS_1").performClick()

        assertEquals(Category.PLANETS, clickedCategory)
        assertEquals("1", clickedId)
    }

    @Test
    fun spokeScreen_clickingStarshipCard_triggersOnNavigateToRelated() {
        var clickedCategory: Category? = null
        var clickedId: String? = null

        val filmEntity = StarWarsEntity.FilmEntity(
            title = "A New Hope",
            episodeId = 4,
            openingCrawl = "It is a period of civil war...",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            url = "https://swapi.info/api/films/1/",
            starships = listOf("https://swapi.info/api/starships/10/")
        )

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    entity = filmEntity,
                    onNavigateToRelated = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    },
                    onNavigateToRelatedEntity = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Starships").performScrollTo()
        composeTestRule.onNodeWithTag("related_card_STARSHIPS_10").performClick()

        assertEquals(Category.STARSHIPS, clickedCategory)
        assertEquals("10", clickedId)
    }

    @Test
    fun spokeScreen_clickingSpeciesCard_triggersOnNavigateToRelated() {
        var clickedCategory: Category? = null
        var clickedId: String? = null

        val filmEntity = StarWarsEntity.FilmEntity(
            title = "A New Hope",
            episodeId = 4,
            openingCrawl = "It is a period of civil war...",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            url = "https://swapi.info/api/films/1/",
            species = listOf("https://swapi.info/api/species/1/")
        )

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    entity = filmEntity,
                    onNavigateToRelated = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    },
                    onNavigateToRelatedEntity = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Species").performScrollTo()
        composeTestRule.onNodeWithTag("related_card_SPECIES_1").performClick()

        assertEquals(Category.SPECIES, clickedCategory)
        assertEquals("1", clickedId)
    }

    @Test
    fun spokeScreen_clickingHomeworldStatCard_triggersOnNavigateToRelated() {
        var clickedCategory: Category? = null
        var clickedId: String? = null

        val personEntity = StarWarsEntity.PersonEntity(
            name = "Luke Skywalker",
            height = "172",
            mass = "77",
            hairColor = "blond",
            skinColor = "fair",
            eyeColor = "blue",
            birthYear = "19BBY",
            gender = "male",
            homeworld = "https://swapi.info/api/planets/1/",
            url = "https://swapi.info/api/people/1/"
        )

        composeTestRule.setContent {
            StarWarsTheme {
                SpokeScreen(
                    entity = personEntity,
                    onNavigateToRelated = { category: Category, id: String ->
                        clickedCategory = category
                        clickedId = id
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Tatooine").performScrollTo().performClick()

        assertEquals(Category.PLANETS, clickedCategory)
        assertEquals("1", clickedId)
    }
}
