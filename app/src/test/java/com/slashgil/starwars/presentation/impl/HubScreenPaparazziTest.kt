package com.slashgil.starwars.presentation.impl

import androidx.compose.animation.ExperimentalSharedTransitionApi
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.presentation.contract.StarWarsUiState
import com.slashgil.starwars.ui.theme.StarWarsTheme
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSharedTransitionApi::class)
class HubScreenPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5
    )

    private val sampleCharacters = listOf(
        Person(
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
        ),
        Person(
            name = "C-3PO",
            height = "167",
            mass = "75",
            hairColor = "n/a",
            skinColor = "gold",
            eyeColor = "yellow",
            birthYear = "112BBY",
            gender = "n/a",
            homeworld = "Tatooine",
            url = "https://swapi.info/api/people/2/"
        )
    )

    @Test
    fun hubScreen_contentState() {
        paparazzi.snapshot {
            StarWarsTheme {
                HubScreen(
                    uiState = StarWarsUiState(
                        isLoading = false,
                        hubCharacters = sampleCharacters
                    ),
                    onIntent = {}
                )
            }
        }
    }

    @Test
    fun hubScreen_loadingState() {
        paparazzi.snapshot {
            StarWarsTheme {
                HubScreen(
                    uiState = StarWarsUiState(isLoading = true),
                    onIntent = {}
                )
            }
        }
    }

    @Test
    fun hubScreen_errorState() {
        paparazzi.snapshot {
            StarWarsTheme {
                HubScreen(
                    uiState = StarWarsUiState(
                        isLoading = false,
                        error = "Failed to load characters"
                    ),
                    onIntent = {}
                )
            }
        }
    }
}
