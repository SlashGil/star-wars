package com.slashgil.starwars.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.slashgil.starwars.domain.contract.ForceSettings

data class StarWarsColors(
    val primary: Color,
    val accent: Color,
    val background: Color = StarWarsBlack,
    val surface: Color = StarWarsBlack,
    val surfaceVariant: Color = StarWarsDarkGray,
    val textPrimary: Color = StarWarsTextPrimary,
    val textSecondary: Color = StarWarsTextSecondary,
    val cardBorder: Color = StarWarsCardBorder,
    val sithRed: Color = StarWarsSithRed,
    val error: Color = Color(0xFFCF6679),
)

val LocalStarWarsColors = staticCompositionLocalOf {
    StarWarsColors(
        primary = StarWarsYellow,
        accent = StarWarsYellow,
    )
}

val LocalStarWarsTypography = staticCompositionLocalOf {
    createStarWarsTypography()
}

object StarWarsTheme {
    val colors: StarWarsColors
        @Composable
        @ReadOnlyComposable
        get() = LocalStarWarsColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalStarWarsTypography.current
}

@Composable
fun StarWarsTheme(
    forceSettings: ForceSettings = ForceSettings(),
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = true,
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val accentColor = forceSettings.accentColor
    val starWarsTypography = createStarWarsTypography(forceSettings.appFont.fontFamily)

    val starWarsColors = StarWarsColors(
        primary = accentColor,
        accent = accentColor,
        background = StarWarsBlack,
        surface = StarWarsBlack,
        surfaceVariant = StarWarsDarkGray,
        textPrimary = StarWarsTextPrimary,
        textSecondary = StarWarsTextSecondary,
        cardBorder = StarWarsCardBorder,
        sithRed = StarWarsSithRed,
        error = Color(0xFFCF6679),
    )

    val materialColorScheme = darkColorScheme(
        background = StarWarsBlack,
        surface = StarWarsBlack,
        surfaceContainer = StarWarsDarkGray,
        surfaceVariant = StarWarsDarkGray,
        primary = accentColor,
        onPrimary = Color.Black,
        secondary = accentColor,
        tertiary = StarWarsSithRed,
        onBackground = StarWarsTextPrimary,
        onSurface = StarWarsTextPrimary,
        onSurfaceVariant = StarWarsTextSecondary,
        outline = accentColor.copy(alpha = 0.5f),
        outlineVariant = StarWarsCardBorder,
        surfaceContainerHigh = StarWarsDarkGray,
        surfaceContainerHighest = StarWarsDarkGray,
    )

    CompositionLocalProvider(
        LocalStarWarsColors provides starWarsColors,
        LocalStarWarsTypography provides starWarsTypography,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = starWarsTypography,
            content = content,
        )
    }
}
