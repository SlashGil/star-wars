package com.slashgil.starwars.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.presentation.mvi.StarWarsIntent
import com.slashgil.starwars.presentation.viewmodel.StarWarsViewModel

sealed interface Route {
    object Hub : Route
    data class Spoke(val character: Person) : Route
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun StarWarsApp(viewModel: StarWarsViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()

    val backStack = remember(uiState.selectedSpokeCharacter) {
        if (uiState.selectedSpokeCharacter != null) {
            listOf(Route.Hub, Route.Spoke(uiState.selectedSpokeCharacter!!))
        } else {
            listOf(Route.Hub)
        }
    }

    val listDetailStrategy = rememberListDetailSceneStrategy<Route>()

    SharedTransitionLayout(modifier = modifier) {
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize(),
            onBack = {
                if (uiState.selectedSpokeCharacter != null) {
                    viewModel.processIntent(StarWarsIntent.OnBackToHub)
                }
            },
            sceneStrategy = listDetailStrategy
        ) { key ->
            when (key) {
                is Route.Hub -> {
                    NavEntry<Route>(
                        key = key,
                        metadata = ListDetailSceneStrategy.listPane(
                            detailPlaceholder = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Select a character",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        )
                    ) {
                        AnimatedVisibility(visible = true) {
                            HubScreen(
                                uiState = uiState,
                                onIntent = viewModel::processIntent,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedVisibility
                            )
                        }
                    }
                }
                is Route.Spoke -> {
                    NavEntry<Route>(
                        key = key,
                        metadata = ListDetailSceneStrategy.detailPane()
                    ) {
                        AnimatedVisibility(visible = true) {
                            SpokeScreen(
                                character = key.character,
                                onBack = { viewModel.processIntent(StarWarsIntent.OnBackToHub) },
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedVisibility
                            )
                        }
                    }
                }
            }
        }
    }
}
