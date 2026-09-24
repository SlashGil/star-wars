package com.slashgil.starwars.presentation.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.slashgil.starwars.R
import com.slashgil.starwars.domain.contract.Category
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.domain.contract.StarWarsEntity
import com.slashgil.starwars.domain.contract.toEntity
import com.slashgil.starwars.domain.impl.StarWarsImageResolverImpl
import com.slashgil.starwars.presentation.contract.StarWarsIntent
import com.slashgil.starwars.presentation.contract.StarWarsUiState
import com.slashgil.starwars.ui.theme.StarWarsBlack
import com.slashgil.starwars.ui.theme.StarWarsCardBorder
import com.slashgil.starwars.ui.theme.StarWarsDarkGray
import com.slashgil.starwars.ui.theme.StarWarsLightsaberBlue
import com.slashgil.starwars.ui.theme.StarWarsSithRed
import com.slashgil.starwars.ui.theme.StarWarsTextPrimary
import com.slashgil.starwars.ui.theme.StarWarsTextSecondary
import com.slashgil.starwars.ui.theme.StarWarsYellow
import com.slashgil.starwars.util.capitalizeWords

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HubScreen(
    uiState: StarWarsUiState,
    onIntent: (StarWarsIntent) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    @Suppress("DEPRECATION")
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    @Suppress("DEPRECATION")
    val isCompact = windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "STAR WARS",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StarWarsBlack,
                    titleContentColor = StarWarsYellow,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { onIntent(StarWarsIntent.Search(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = {
                    Text(
                        text = "Search characters...",
                        color = StarWarsTextSecondary,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = StarWarsYellow,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = StarWarsDarkGray,
                    unfocusedContainerColor = StarWarsDarkGray,
                    disabledContainerColor = StarWarsDarkGray,
                    focusedBorderColor = StarWarsYellow,
                    unfocusedBorderColor = StarWarsCardBorder,
                    cursorColor = StarWarsYellow,
                    focusedTextColor = StarWarsTextPrimary,
                    unfocusedTextColor = StarWarsTextPrimary,
                    focusedLeadingIconColor = StarWarsYellow,
                    unfocusedLeadingIconColor = StarWarsYellow,
                ),
            )

            // Category Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(Category.entries) { category ->
                    val isSelected = category == uiState.selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { onIntent(StarWarsIntent.SelectCategory(category)) },
                        modifier = Modifier.heightIn(min = 36.dp),
                        label = {
                            Text(
                                text = category.displayName,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = StarWarsBlack,
                            labelColor = StarWarsTextSecondary,
                            selectedContainerColor = StarWarsDarkGray, // #1A1A1A
                            selectedLabelColor = StarWarsYellow, // #FFE81F
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = StarWarsCardBorder,
                            selectedBorderColor = StarWarsYellow,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            val displayEntities = uiState.displayEntities

            if (uiState.isLoading && displayEntities.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = StarWarsYellow)
                }
            } else if ((uiState.error != null) && displayEntities.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    items(displayEntities) { entity ->
                        EntityItem(
                            entity = entity,
                            onClick = {
                                if (entity is StarWarsEntity.PersonEntity) {
                                    onIntent(StarWarsIntent.OnCharacterClicked(entity.url))
                                } else {
                                    onIntent(StarWarsIntent.OnEntityClicked(entity.url))
                                }
                            },
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            isCompact = isCompact,
                        )
                    }
                    if (uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(color = StarWarsYellow)
                            }
                        }
                    } else if (!uiState.hasReachedEnd && !uiState.isSearchMode && displayEntities.isNotEmpty()) {
                        item {
                            LaunchedEffect(Unit) {
                                onIntent(StarWarsIntent.LoadMore)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EntityItem(
    entity: StarWarsEntity,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    isCompact: Boolean = true,
) {
    val baseModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .clickable { onClick() }

    val cardModifier = baseModifier.safeSharedElement(
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        key = "card_${entity.url}",
        enabled = isCompact,
    )

    Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = StarWarsDarkGray,
        ),
        border = BorderStroke(1.dp, StarWarsCardBorder),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val context = LocalContext.current
            val resolver = remember(context) { StarWarsImageResolverImpl(context) }
            val imageUrl = remember(entity) { resolver.resolveEntityImageUrl(entity) }

            val fallbackIcon = when (entity.category) {
                Category.PEOPLE -> Icons.Default.Person
                Category.STARSHIPS -> Icons.Default.RocketLaunch
                Category.PLANETS -> Icons.Default.Public
                Category.SPECIES -> Icons.Default.Pets
                Category.FILMS -> Icons.Default.Movie
            }
            val placeholderPainter = rememberVectorPainter(fallbackIcon)
            val errorPainter = rememberVectorPainter(fallbackIcon)

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.gemini_svg)
                    .error(R.drawable.gemini_svg)
                    .build(),
                placeholder = placeholderPainter,
                error = errorPainter,
                contentDescription = entity.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(StarWarsBlack),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                val textModifier = Modifier.safeSharedElement(
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    key = "title_${entity.url}",
                    enabled = isCompact,
                )

                Text(
                    text = entity.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = StarWarsTextPrimary,
                    modifier = textModifier,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    when (entity) {
                        is StarWarsEntity.PersonEntity -> {
                            AttributeBadge(
                                text = entity.gender.capitalizeWords(),
                                accentColor = StarWarsLightsaberBlue,
                            )
                            if (entity.birthYear.isNotEmpty() && (entity.birthYear != "unknown")) {
                                AttributeBadge(
                                    text = entity.birthYear.capitalizeWords(),
                                    accentColor = StarWarsSithRed,
                                )
                            }
                        }
                        is StarWarsEntity.StarshipEntity -> {
                            if (entity.starshipClass.isNotEmpty()) {
                                AttributeBadge(
                                    text = entity.starshipClass.capitalizeWords(),
                                    accentColor = StarWarsLightsaberBlue,
                                )
                            }
                            if (entity.model.isNotEmpty()) {
                                AttributeBadge(
                                    text = entity.model.capitalizeWords(),
                                    accentColor = StarWarsYellow,
                                )
                            }
                        }
                        is StarWarsEntity.PlanetEntity -> {
                            if (entity.climate.isNotEmpty()) {
                                AttributeBadge(
                                    text = entity.climate.capitalizeWords(),
                                    accentColor = StarWarsLightsaberBlue,
                                )
                            }
                            if (entity.terrain.isNotEmpty()) {
                                AttributeBadge(
                                    text = entity.terrain.capitalizeWords(),
                                    accentColor = StarWarsSithRed,
                                )
                            }
                        }
                        is StarWarsEntity.SpeciesEntity -> {
                            if (entity.classification.isNotEmpty()) {
                                AttributeBadge(
                                    text = entity.classification.capitalizeWords(),
                                    accentColor = StarWarsLightsaberBlue,
                                )
                            }
                            if (entity.language.isNotEmpty()) {
                                AttributeBadge(
                                    text = entity.language.capitalizeWords(),
                                    accentColor = StarWarsYellow,
                                )
                            }
                        }
                        is StarWarsEntity.FilmEntity -> {
                            AttributeBadge(
                                text = "Episode ${entity.episodeId}",
                                accentColor = StarWarsYellow,
                            )
                            if (entity.releaseDate.isNotEmpty()) {
                                AttributeBadge(
                                    text = entity.releaseDate,
                                    accentColor = StarWarsLightsaberBlue,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CharacterItem(
    character: Person,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    isCompact: Boolean = true,
) {
    EntityItem(
        entity = character.toEntity(),
        onClick = onClick,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        isCompact = isCompact,
    )
}

@Composable
fun AttributeBadge(
    text: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.heightIn(min = 36.dp),
        color = StarWarsBlack,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.6f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(accentColor),
            )
            Text(
                text = text.capitalizeWords(),
                style = MaterialTheme.typography.labelSmall,
                color = StarWarsTextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
