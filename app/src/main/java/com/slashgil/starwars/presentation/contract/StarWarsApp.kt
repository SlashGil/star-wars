package com.slashgil.starwars.presentation.contract

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.presentation.impl.AdaptiveArchiveContent
import com.slashgil.starwars.presentation.impl.SettingsScreen
import com.slashgil.starwars.presentation.impl.StarWarsViewModel
import com.slashgil.starwars.ui.theme.StarWarsTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun StarWarsApp(viewModel: StarWarsViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<StarWarsEntity>()
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = uiState.isSettingsOpen || uiState.navigationStack.isNotEmpty() || navigator.canNavigateBack()) {
        coroutineScope.launch {
            if (uiState.isSettingsOpen) {
                viewModel.processIntent(StarWarsIntent.CloseSettings)
            } else if (uiState.navigationStack.isNotEmpty()) {
                viewModel.processIntent(StarWarsIntent.OnNavigateBack)
            } else if (navigator.canNavigateBack()) {
                navigator.navigateBack()
                viewModel.processIntent(StarWarsIntent.OnBackToHub)
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = StarWarsTheme.colors.background,
    ) {
        if (uiState.isSettingsOpen) {
            SettingsScreen(
                forceSettings = uiState.forceSettings,
                onSetAllegiance = { viewModel.processIntent(StarWarsIntent.SetAllegiance(it)) },
                onSetLightsaberColor = { viewModel.processIntent(StarWarsIntent.SetLightsaberColor(it)) },
                onSetAppFont = { viewModel.processIntent(StarWarsIntent.SetAppFont(it)) },
                onBack = { viewModel.processIntent(StarWarsIntent.CloseSettings) },
            )
        } else {
            AdaptiveArchiveContent(
                uiState = uiState,
                onIntent = viewModel::processIntent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
