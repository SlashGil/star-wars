package com.slashgil.starwars.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val StarWarsColorScheme = darkColorScheme(
    background = StarWarsBlack,
    surface = StarWarsBlack,
    surfaceContainer = StarWarsDarkGray,
    surfaceVariant = StarWarsDarkGray,
    primary = StarWarsYellow,
    onPrimary = Color.Black,
    secondary = StarWarsLightsaberBlue,
    tertiary = StarWarsSithRed,
    onBackground = StarWarsTextPrimary,
    onSurface = StarWarsTextPrimary,
    onSurfaceVariant = StarWarsTextSecondary,
    outline = StarWarsCardBorder,
    outlineVariant = StarWarsCardBorder,
    surfaceContainerHigh = StarWarsDarkGray,
    surfaceContainerHighest = StarWarsDarkGray,
)

@Composable
fun StarWarsTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = true,
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = StarWarsColorScheme,
        typography = Typography,
        content = content,
    )
}
