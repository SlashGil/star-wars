package com.slashgil.starwars.presentation.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.ui.theme.StarWarsTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalSharedTransitionApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SpokeScreenRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
        url = "https://swapi.dev/api/people/4/"
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
        composeTestRule.onNodeWithText("202").assertIsDisplayed()
        composeTestRule.onNodeWithText("136").assertIsDisplayed()
        composeTestRule.onNodeWithText("41.9BBY").assertExists()
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
}
