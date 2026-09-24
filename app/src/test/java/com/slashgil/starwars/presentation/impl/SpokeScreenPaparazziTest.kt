package com.slashgil.starwars.presentation.impl

import androidx.compose.animation.ExperimentalSharedTransitionApi
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.ui.theme.StarWarsTheme
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSharedTransitionApi::class)
class SpokeScreenPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5
    )

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
    fun spokeScreen_characterDetail() {
        paparazzi.snapshot {
            StarWarsTheme {
                SpokeScreen(
                    character = sampleCharacter,
                    onBack = {}
                )
            }
        }
    }
}
