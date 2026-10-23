package com.slashgil.starwars.presentation.impl

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.presentation.contract.StarWarsIntent
import com.slashgil.starwars.presentation.contract.StarWarsUiState
import com.slashgil.starwars.ui.theme.StarWarsTheme

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AdaptiveArchiveContent(
    uiState: StarWarsUiState,
    onIntent: (StarWarsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<StarWarsEntity>()
    val selectedEntity = uiState.displaySelectedEntity

    LaunchedEffect(selectedEntity) {
        if (selectedEntity != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        } else if (navigator.canNavigateBack()) {
            navigator.navigateBack()
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
                    onIntent = onIntent,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                if (selectedEntity != null) {
                    SpokeScreen(
                        entity = selectedEntity,
                        onBack = {
                            onIntent(StarWarsIntent.OnNavigateBack)
                        },
                        onNavigateBack = {
                            onIntent(StarWarsIntent.OnNavigateBack)
                        },
                        onNavigateToRelated = { category, id ->
                            onIntent(StarWarsIntent.NavigateToRelatedEntity(category, id))
                        },
                        onNavigateToRelatedEntity = { category, id ->
                            onIntent(StarWarsIntent.NavigateToRelatedEntity(category, id))
                        },
                    )
                } else {
                    DetailPlaceholder(selectedCategory = uiState.selectedCategory)
                }
            }
        },
    )
}

@Composable
fun DetailPlaceholder(
    selectedCategory: Category,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StarWarsTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        val placeholderText = if (selectedCategory == Category.PEOPLE) {
            "Select a character to view details"
        } else {
            "Select an item to view details"
        }
        Text(
            text = placeholderText,
            style = StarWarsTheme.typography.titleLarge,
            color = StarWarsTheme.colors.textSecondary,
        )
    }
}
