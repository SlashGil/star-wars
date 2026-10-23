package com.slashgil.starwars.domain.contract

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.slashgil.starwars.R

enum class ForceAllegiance {
    SITH,
    JEDI
}

enum class LightsaberColor(val displayName: String, val hexColor: Long) {
    BLUE("Jedi Blue", 0xFF00E5FF),
    GREEN("Consular Green", 0xFF00FF66),
    PURPLE("Mace Purple", 0xFFA000FF),
    YELLOW("Sentinel Yellow", 0xFFFFE81F),
    CYAN("Cal Cyan", 0xFF00FFFF),
    MAGENTA("Mara Magenta", 0xFFFF007F),
    WHITE("Ahsoka White", 0xFFF5F5F5);

    val color: Color get() = Color(hexColor)
}

enum class AppFont(val displayName: String, val fontResId: Int) {
    DEFAULT("Default System", 0),
    STAR_JEDI("Star Jedi", R.font.star_jedi),
    STAR_JEDI_OUTLINE("Star Jedi Outline", R.font.star_jedi_outline),
    STAR_JEDI_HOLLOW("Star Jedi Hollow", R.font.star_jedi_hollow),
    STAR_JEDI_ROUNDED("Star Jedi Rounded", R.font.star_jedi_rounded),
    DEATH_STAR("Death Star", R.font.death_star),
    MOONHOUSE("Moonhouse", R.font.moonhouse);

    val fontFamily: FontFamily
        get() = if (fontResId != 0) FontFamily(Font(fontResId)) else FontFamily.Default
}

data class ForceSettings(
    val allegiance: ForceAllegiance = ForceAllegiance.JEDI,
    val jediLightsaberColor: LightsaberColor = LightsaberColor.BLUE,
    val appFont: AppFont = AppFont.DEFAULT,
) {
    val accentColor: Color
        get() = when (allegiance) {
            ForceAllegiance.SITH -> Color(0xFFEB212E)
            ForceAllegiance.JEDI -> jediLightsaberColor.color
        }
}
