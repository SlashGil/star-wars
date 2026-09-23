package com.slashgil.starwars.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.presentation.mvi.StarWarsIntent
import com.slashgil.starwars.presentation.viewmodel.StarWarsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun StarWarsApp(viewModel: StarWarsViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<Person>()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.selectedSpokeCharacter) {
        if (uiState.selectedSpokeCharacter != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        } else if (navigator.canNavigateBack()) {
            navigator.navigateBack()
        }
    }

    BackHandler(enabled = navigator.canNavigateBack()) {
        coroutineScope.launch {
            navigator.navigateBack()
            viewModel.processIntent(StarWarsIntent.OnBackToHub)
        }
    }

    ListDetailPaneScaffold(
        modifier = modifier.fillMaxSize(),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                HubScreen(
                    uiState = uiState,
                    onIntent = viewModel::processIntent
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedCharacter = uiState.selectedSpokeCharacter
                if (selectedCharacter != null) {
                    SpokeScreen(
                        character = selectedCharacter,
                        onBack = {
                            coroutineScope.launch {
                                if (navigator.canNavigateBack()) {
                                    navigator.navigateBack()
                                }
                                viewModel.processIntent(StarWarsIntent.OnBackToHub)
                            }
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Select a character to view details",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}
