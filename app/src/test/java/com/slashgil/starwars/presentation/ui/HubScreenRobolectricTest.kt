package com.slashgil.starwars.presentation.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.presentation.mvi.StarWarsIntent
import com.slashgil.starwars.presentation.mvi.StarWarsUiState
import com.slashgil.starwars.ui.theme.StarWarsTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalSharedTransitionApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class HubScreenRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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

    @Test
    fun hubScreen_displaysCharacterNames() {
        composeTestRule.setContent {
            StarWarsTheme {
                HubScreen(
                    uiState = StarWarsUiState(
                        isLoading = false,
                        hubCharacters = listOf(sampleCharacter)
                    ),
                    onIntent = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Luke Skywalker").assertIsDisplayed()
        composeTestRule.onNodeWithText("male").assertIsDisplayed()
    }

    @Test
    fun hubScreen_clickingCharacter_triggersIntent() {
        var receivedIntent: StarWarsIntent? = null

        composeTestRule.setContent {
            StarWarsTheme {
                HubScreen(
                    uiState = StarWarsUiState(
                        isLoading = false,
                        hubCharacters = listOf(sampleCharacter)
                    ),
                    onIntent = { receivedIntent = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Luke Skywalker").performClick()

        assertTrue(receivedIntent is StarWarsIntent.OnCharacterClicked)
        assertEquals(sampleCharacter.url, (receivedIntent as StarWarsIntent.OnCharacterClicked).characterUrl)
    }

    @Test
    fun hubScreen_typingInSearch_triggersSearchIntent() {
        var receivedIntent: StarWarsIntent? = null

        composeTestRule.setContent {
            StarWarsTheme {
                HubScreen(
                    uiState = StarWarsUiState(
                        isLoading = false,
                        hubCharacters = listOf(sampleCharacter)
                    ),
                    onIntent = { receivedIntent = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Search characters...").performTextInput("Yoda")

        assertTrue(receivedIntent is StarWarsIntent.Search)
        assertEquals("Yoda", (receivedIntent as StarWarsIntent.Search).query)
    }
}
