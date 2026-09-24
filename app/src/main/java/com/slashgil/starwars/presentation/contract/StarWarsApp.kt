package com.slashgil.starwars.presentation.contract

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.presentation.impl.HubScreen
import com.slashgil.starwars.presentation.impl.SpokeScreen
import com.slashgil.starwars.presentation.impl.StarWarsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun StarWarsApp(viewModel: StarWarsViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<StarWarsEntity>()
    val coroutineScope = rememberCoroutineScope()

    val selectedEntity = uiState.displaySelectedEntity

    LaunchedEffect(selectedEntity) {
        if (selectedEntity != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        } else if (navigator.canNavigateBack()) {
            navigator.navigateBack()
        }
    }

    BackHandler(enabled = uiState.navigationStack.isNotEmpty() || navigator.canNavigateBack()) {
        coroutineScope.launch {
            if (uiState.navigationStack.isNotEmpty()) {
                viewModel.processIntent(StarWarsIntent.OnNavigateBack)
            } else if (navigator.canNavigateBack()) {
                navigator.navigateBack()
                viewModel.processIntent(StarWarsIntent.OnBackToHub)
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        ListDetailPaneScaffold(
            modifier = Modifier.fillMaxSize(),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    HubScreen(
                        uiState = uiState,
                        onIntent = viewModel::processIntent,
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    if (selectedEntity != null) {
                        SpokeScreen(
                            entity = selectedEntity,
                            onBack = {
                                viewModel.processIntent(StarWarsIntent.OnNavigateBack)
                            },
                            onNavigateBack = {
                                viewModel.processIntent(StarWarsIntent.OnNavigateBack)
                            },
                            onNavigateToRelated = { category, id ->
                                viewModel.processIntent(StarWarsIntent.NavigateToRelatedEntity(category, id))
                            },
                            onNavigateToRelatedEntity = { category, id ->
                                viewModel.processIntent(StarWarsIntent.NavigateToRelatedEntity(category, id))
                            },
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center,
                        ) {
                            val placeholderText = if (uiState.selectedCategory == Category.PEOPLE) {
                                "Select a character to view details"
                            } else {
                                "Select an item to view details"
                            }
                            Text(
                                text = placeholderText,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            },
        )
    }
}
