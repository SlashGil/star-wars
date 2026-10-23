package com.slashgil.starwars.presentation.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.slashgil.starwars.ui.theme.StarWarsBlack
import com.slashgil.starwars.ui.theme.StarWarsCardBorder
import com.slashgil.starwars.ui.theme.StarWarsDarkGray
import com.slashgil.starwars.ui.theme.StarWarsLightsaberBlue
import com.slashgil.starwars.ui.theme.StarWarsSithRed
import com.slashgil.starwars.ui.theme.StarWarsTextPrimary
import com.slashgil.starwars.ui.theme.StarWarsTextSecondary
import com.slashgil.starwars.ui.theme.StarWarsTheme
import com.slashgil.starwars.util.capitalizeWords

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpokeScreen(
    character: Person,
    onBack: () -> Unit = {},
    onNavigateBack: () -> Unit = onBack,
    onNavigateToRelated: (Category, String) -> Unit = { _, _ -> },
    onNavigateToRelatedEntity: (Category, String) -> Unit = onNavigateToRelated,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    SpokeScreen(
        entity = character.toEntity(),
        onBack = onBack,
        onNavigateBack = onNavigateBack,
        onNavigateToRelated = onNavigateToRelated,
        onNavigateToRelatedEntity = onNavigateToRelatedEntity,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpokeScreen(
    entity: StarWarsEntity,
    onBack: () -> Unit = {},
    onNavigateBack: () -> Unit = onBack,
    onNavigateToRelated: (Category, String) -> Unit = { _, _ -> },
    onNavigateToRelatedEntity: (Category, String) -> Unit = onNavigateToRelated,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val handleNavigateBack = {
        onNavigateBack()
        if (onBack != onNavigateBack) {
            onBack()
        }
    }

    val handleNavigateToRelated: (Category, String) -> Unit = { category, id ->
        onNavigateToRelated(category, id)
        if (onNavigateToRelatedEntity !== onNavigateToRelated) {
            onNavigateToRelatedEntity(category, id)
        }
    }

    @Suppress("DEPRECATION")
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    @Suppress("DEPRECATION")
    val isCompact = windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    val scaffoldModifier = Modifier.safeSharedElement(
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        key = "card_${entity.url}",
        enabled = isCompact,
    )

    Scaffold(
        modifier = scaffoldModifier,
        containerColor = StarWarsTheme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    val titleModifier = Modifier.safeSharedElement(
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        key = "title_${entity.url}",
                        enabled = isCompact,
                    )
                    Text(
                        text = entity.name,
                        style = StarWarsTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = titleModifier,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = handleNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Hub",
                            tint = StarWarsTheme.colors.primary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StarWarsBlack,
                    titleContentColor = StarWarsTheme.colors.primary,
                    navigationIconContentColor = StarWarsTheme.colors.primary,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
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

            // Header with prominent portrait image loaded via Coil
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = StarWarsDarkGray,
                ),
                border = BorderStroke(1.dp, StarWarsCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(StarWarsDarkGray),
                    contentAlignment = Alignment.Center,
                ) {
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
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                    )
                }
            }

            // Title & Subtitle tailored to category
            val subtitleText = when (entity) {
                is StarWarsEntity.PersonEntity -> "${entity.gender.capitalizeWords()} • Born ${entity.birthYear.capitalizeWords()}"
                is StarWarsEntity.StarshipEntity -> "${entity.model.capitalizeWords()} • ${entity.starshipClass.capitalizeWords()}"
                is StarWarsEntity.PlanetEntity -> "${entity.climate.capitalizeWords()} • ${entity.terrain.capitalizeWords()}"
                is StarWarsEntity.SpeciesEntity -> "${entity.classification.capitalizeWords()} • ${entity.designation.capitalizeWords()}"
                is StarWarsEntity.FilmEntity -> "Director: ${entity.director} • Released: ${entity.releaseDate}"
            }

            if (subtitleText.isNotBlank()) {
                Text(
                    text = subtitleText,
                    style = StarWarsTheme.typography.bodyMedium,
                    color = StarWarsTextSecondary,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }

            // 2-Column Layout for Info/Stats
            Text(
                text = "${entity.category.displayName} Details",
                style = StarWarsTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = StarWarsTheme.colors.primary,
                modifier = Modifier.padding(vertical = 4.dp),
            )

            val attributesList: List<AttributeItem> = when (entity) {
                is StarWarsEntity.PersonEntity -> {
                    val homeworldPlanetId = extractPlanetId(entity.homeworld)
                    val homeworldOnClick: (() -> Unit)? = homeworldPlanetId?.let { id ->
                        { handleNavigateToRelated(Category.PLANETS, id) }
                    }
                    listOf(
                        AttributeItem("Height", entity.height.capitalizeWords()),
                        AttributeItem("Mass", entity.mass.capitalizeWords()),
                        AttributeItem("Hair Color", entity.hairColor.capitalizeWords()),
                        AttributeItem("Skin Color", entity.skinColor.capitalizeWords()),
                        AttributeItem("Eye Color", entity.eyeColor.capitalizeWords()),
                        AttributeItem("Birth Year", entity.birthYear.capitalizeWords()),
                        AttributeItem("Gender", entity.gender.capitalizeWords()),
                        AttributeItem(
                            label = "Homeworld",
                            value = resolver.resolvePlanetName(entity.homeworld).capitalizeWords(),
                            onClick = homeworldOnClick,
                        ),
                    )
                }
                is StarWarsEntity.StarshipEntity -> listOf(
                    AttributeItem("Model", entity.model.capitalizeWords()),
                    AttributeItem("Class", entity.starshipClass.capitalizeWords()),
                    AttributeItem("Cost", entity.costInCredits.capitalizeWords()),
                    AttributeItem("Length", entity.length.capitalizeWords()),
                    AttributeItem("Crew", entity.crew.capitalizeWords()),
                    AttributeItem("Passengers", entity.passengers.capitalizeWords()),
                    AttributeItem("Speed", entity.maxAtmospheringSpeed.capitalizeWords()),
                    AttributeItem("Hyperdrive Rating", entity.hyperdriveRating.capitalizeWords()),
                )
                is StarWarsEntity.PlanetEntity -> listOf(
                    AttributeItem("Climate", entity.climate.capitalizeWords()),
                    AttributeItem("Terrain", entity.terrain.capitalizeWords()),
                    AttributeItem("Population", entity.population.capitalizeWords()),
                    AttributeItem("Diameter", entity.diameter.capitalizeWords()),
                    AttributeItem("Gravity", entity.gravity.capitalizeWords()),
                    AttributeItem("Orbital Period", entity.orbitalPeriod.capitalizeWords()),
                    AttributeItem("Surface Water", entity.surfaceWater.capitalizeWords()),
                )
                is StarWarsEntity.SpeciesEntity -> {
                    val speciesHomeworldPlanetId = extractPlanetId(entity.homeworld)
                    val speciesHomeworldOnClick: (() -> Unit)? = speciesHomeworldPlanetId?.let { id ->
                        { handleNavigateToRelated(Category.PLANETS, id) }
                    }
                    listOf(
                        AttributeItem("Classification", entity.classification.capitalizeWords()),
                        AttributeItem("Designation", entity.designation.capitalizeWords()),
                        AttributeItem("Language", entity.language.capitalizeWords()),
                        AttributeItem("Lifespan", entity.averageLifespan.capitalizeWords()),
                        AttributeItem("Height", entity.averageHeight.capitalizeWords()),
                        AttributeItem("Eye Colors", entity.eyeColors.capitalizeWords()),
                        AttributeItem(
                            label = "Homeworld",
                            value = (entity.homeworld?.let { resolver.resolvePlanetName(it) } ?: "unknown").capitalizeWords(),
                            onClick = speciesHomeworldOnClick,
                        ),
                    )
                }
                is StarWarsEntity.FilmEntity -> listOf(
                    AttributeItem("Episode", entity.episodeId.toString()),
                    AttributeItem("Director", entity.director),
                    AttributeItem("Producer", entity.producer),
                    AttributeItem("Release Date", entity.releaseDate),
                    AttributeItem("Opening Crawl", entity.openingCrawl),
                )
            }

            val shortAttrs = attributesList.filter { it.value.length <= 40 }
            val longAttrs = attributesList.filter { it.value.length > 40 }

            shortAttrs.chunked(2).forEachIndexed { index, pair ->
                val accent1 = if ((index % 2) == 0) StarWarsLightsaberBlue else StarWarsSithRed
                val accent2 = if ((index % 2) == 0) StarWarsSithRed else StarWarsLightsaberBlue

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StatCard(
                        label = pair[0].label,
                        value = pair[0].value,
                        accentColor = accent1,
                        onClick = pair[0].onClick,
                        modifier = Modifier.weight(1f),
                    )
                    if (pair.size > 1) {
                        StatCard(
                            label = pair[1].label,
                            value = pair[1].value,
                            accentColor = accent2,
                            onClick = pair[1].onClick,
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            longAttrs.forEach { attr ->
                Spacer(modifier = Modifier.height(4.dp))
                StatCard(
                    label = attr.label,
                    value = attr.value,
                    accentColor = StarWarsTheme.colors.primary,
                    onClick = attr.onClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Related Items / Appearances Section
            val filmList = when (entity) {
                is StarWarsEntity.PersonEntity -> entity.films
                is StarWarsEntity.StarshipEntity -> entity.films
                is StarWarsEntity.PlanetEntity -> entity.films
                is StarWarsEntity.SpeciesEntity -> entity.films
                is StarWarsEntity.FilmEntity -> emptyList()
            }

            val characterList = when (entity) {
                is StarWarsEntity.FilmEntity -> entity.characters
                is StarWarsEntity.StarshipEntity -> entity.pilots
                is StarWarsEntity.PlanetEntity -> entity.residents
                is StarWarsEntity.SpeciesEntity -> entity.people
                else -> emptyList()
            }

            val planetList = when (entity) {
                is StarWarsEntity.FilmEntity -> entity.planets
                else -> emptyList()
            }

            val starshipList = when (entity) {
                is StarWarsEntity.FilmEntity -> entity.starships
                else -> emptyList()
            }

            val speciesList = when (entity) {
                is StarWarsEntity.FilmEntity -> entity.species
                else -> emptyList()
            }

            if (filmList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Film Appearances",
                    style = StarWarsTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = StarWarsTheme.colors.primary,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                ) {
                    items(filmList) { filmUrl ->
                        val filmId = filmUrl.trimEnd('/').substringAfterLast('/')
                        FilmCard(
                            filmUrl = filmUrl,
                            onClick = { handleNavigateToRelated(Category.FILMS, filmId) },
                            modifier = Modifier.testTag("related_card_${Category.FILMS}_$filmId")
                        )
                    }
                }
            }

            if (characterList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                val title = when (entity) {
                    is StarWarsEntity.StarshipEntity -> "Pilots"
                    is StarWarsEntity.PlanetEntity -> "Residents"
                    is StarWarsEntity.SpeciesEntity -> "People"
                    else -> "Characters"
                }
                Text(
                    text = title,
                    style = StarWarsTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = StarWarsTheme.colors.primary,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                ) {
                    items(characterList) { charUrl ->
                        val charId = charUrl.trimEnd('/').substringAfterLast('/')
                        CharacterCard(
                            characterUrl = charUrl,
                            onClick = { handleNavigateToRelated(Category.PEOPLE, charId) },
                            modifier = Modifier.testTag("related_card_${Category.PEOPLE}_$charId")
                        )
                    }
                }
            }

            if (planetList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Planets",
                    style = StarWarsTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = StarWarsTheme.colors.primary,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                ) {
                    items(planetList) { planetUrl ->
                        val planetId = planetUrl.trimEnd('/').substringAfterLast('/')
                        PlanetCard(
                            planetUrl = planetUrl,
                            onClick = { handleNavigateToRelated(Category.PLANETS, planetId) },
                            modifier = Modifier.testTag("related_card_${Category.PLANETS}_$planetId")
                        )
                    }
                }
            }

            if (starshipList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Starships",
                    style = StarWarsTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = StarWarsTheme.colors.primary,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                ) {
                    items(starshipList) { starshipUrl ->
                        val starshipId = starshipUrl.trimEnd('/').substringAfterLast('/')
                        StarshipCard(
                            starshipUrl = starshipUrl,
                            onClick = { handleNavigateToRelated(Category.STARSHIPS, starshipId) },
                            modifier = Modifier.testTag("related_card_${Category.STARSHIPS}_$starshipId")
                        )
                    }
                }
            }

            if (speciesList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Species",
                    style = StarWarsTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = StarWarsTheme.colors.primary,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                ) {
                    items(speciesList) { speciesUrl ->
                        val speciesId = speciesUrl.trimEnd('/').substringAfterLast('/')
                        SpeciesCard(
                            speciesUrl = speciesUrl,
                            onClick = { handleNavigateToRelated(Category.SPECIES, speciesId) },
                            modifier = Modifier.testTag("related_card_${Category.SPECIES}_$speciesId")
                        )
                    }
                }
            }
        }
    }
}

private fun extractPlanetId(url: String?): String? {
    if (url.isNullOrEmpty()) return null
    return url.trimEnd('/').substringAfterLast('/')
}

private data class AttributeItem(
    val label: String,
    val value: String,
    val onClick: (() -> Unit)? = null,
)

@Composable
fun StatCard(
    label: String,
    value: String,
    accentColor: Color,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = StarWarsDarkGray,
        ),
        border = BorderStroke(1.dp, if (onClick != null) accentColor else StarWarsCardBorder),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = label.uppercase(),
                    style = StarWarsTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = accentColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Surface(
                    color = accentColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, accentColor),
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(accentColor),
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value.ifEmpty { "unknown" }.capitalizeWords(),
                style = StarWarsTheme.typography.bodyLarge,
                color = StarWarsTextPrimary,
            )
        }
    }
}

@Composable
fun FilmCard(
    filmUrl: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val placeholderPainter = rememberVectorPainter(Icons.Default.Movie)
    val errorPainter = rememberVectorPainter(Icons.Default.Movie)
    val resolver = remember(context) { StarWarsImageResolverImpl(context) }
    val film = remember(filmUrl) { resolver.resolveFilm(filmUrl) }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(130.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = StarWarsDarkGray,
        ),
        border = BorderStroke(1.dp, StarWarsCardBorder),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(film.posterUrl)
                    .crossfade(false)
                    .placeholder(R.drawable.gemini_svg)
                    .error(R.drawable.gemini_svg)
                    .build(),
                placeholder = placeholderPainter,
                error = errorPainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(StarWarsBlack),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = film.title,
                style = StarWarsTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = StarWarsTextPrimary,
            )
        }
    }
}

@Composable
fun CharacterCard(
    characterUrl: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val placeholderPainter = rememberVectorPainter(Icons.Default.Person)
    val errorPainter = rememberVectorPainter(Icons.Default.Person)
    val resolver = remember(context) { StarWarsImageResolverImpl(context) }
    val characterName = remember(characterUrl) { resolver.resolveCharacterName(characterUrl) }
    val imageUrl = remember(characterUrl) { resolver.resolveCharacterImageUrl(characterUrl) }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(120.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = StarWarsDarkGray,
        ),
        border = BorderStroke(1.dp, StarWarsCardBorder),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(false)
                    .placeholder(R.drawable.gemini_svg)
                    .error(R.drawable.gemini_svg)
                    .build(),
                placeholder = placeholderPainter,
                error = errorPainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(StarWarsBlack),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = characterName,
                style = StarWarsTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = StarWarsTextPrimary,
            )
        }
    }
}

@Composable
fun PlanetCard(
    planetUrl: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val placeholderPainter = rememberVectorPainter(Icons.Default.Public)
    val errorPainter = rememberVectorPainter(Icons.Default.Public)
    val resolver = remember(context) { StarWarsImageResolverImpl(context) }
    val planet = remember(planetUrl) { resolver.resolvePlanetName(planetUrl) }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(110.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = StarWarsDarkGray,
        ),
        border = BorderStroke(1.dp, StarWarsCardBorder),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(StarWarsBlack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = placeholderPainter,
                    contentDescription = null,
                    tint = StarWarsLightsaberBlue,
                    modifier = Modifier.size(32.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = planet,
                style = StarWarsTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = StarWarsTextPrimary,
            )
        }
    }
}

@Composable
fun StarshipCard(
    starshipUrl: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val placeholderPainter = rememberVectorPainter(Icons.Default.RocketLaunch)
    val resolver = remember(context) { StarWarsImageResolverImpl(context) }
    val starshipName = remember(starshipUrl) { resolver.resolveStarshipName(starshipUrl) }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(120.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = StarWarsDarkGray,
        ),
        border = BorderStroke(1.dp, StarWarsCardBorder),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(StarWarsBlack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = placeholderPainter,
                    contentDescription = null,
                    tint = StarWarsTheme.colors.primary,
                    modifier = Modifier.size(32.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = starshipName,
                style = StarWarsTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = StarWarsTextPrimary,
            )
        }
    }
}

@Composable
fun SpeciesCard(
    speciesUrl: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val placeholderPainter = rememberVectorPainter(Icons.Default.Pets)
    val resolver = remember(context) { StarWarsImageResolverImpl(context) }
    val speciesName = remember(speciesUrl) { resolver.resolveSpeciesName(speciesUrl) }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(110.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = StarWarsDarkGray,
        ),
        border = BorderStroke(1.dp, StarWarsCardBorder),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(StarWarsBlack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = placeholderPainter,
                    contentDescription = null,
                    tint = StarWarsSithRed,
                    modifier = Modifier.size(32.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = speciesName,
                style = StarWarsTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = StarWarsTextPrimary,
            )
        }
    }
}
