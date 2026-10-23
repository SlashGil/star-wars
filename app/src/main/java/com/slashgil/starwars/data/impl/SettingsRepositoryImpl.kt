package com.slashgil.starwars.data.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.slashgil.starwars.data.contract.SettingsRepository
import com.slashgil.starwars.domain.contract.AppFont
import com.slashgil.starwars.domain.contract.ForceAllegiance
import com.slashgil.starwars.domain.contract.ForceSettings
import com.slashgil.starwars.domain.contract.LightsaberColor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "starwars_settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {

    private object PreferencesKeys {
        val ALLEGIANCE = stringPreferencesKey("force_allegiance")
        val LIGHTSABER_COLOR = stringPreferencesKey("lightsaber_color")
        val APP_FONT = stringPreferencesKey("app_font")
    }

    override val forceSettingsFlow: Flow<ForceSettings> = context.dataStore.data.map { preferences ->
        val allegianceName = preferences[PreferencesKeys.ALLEGIANCE] ?: ForceAllegiance.JEDI.name
        val colorName = preferences[PreferencesKeys.LIGHTSABER_COLOR] ?: LightsaberColor.BLUE.name
        val fontName = preferences[PreferencesKeys.APP_FONT] ?: AppFont.DEFAULT.name

        val allegiance = try {
            ForceAllegiance.valueOf(allegianceName)
        } catch (_: Exception) {
            ForceAllegiance.JEDI
        }

        val color = try {
            LightsaberColor.valueOf(colorName)
        } catch (_: Exception) {
            LightsaberColor.BLUE
        }

        val font = try {
            AppFont.valueOf(fontName)
        } catch (_: Exception) {
            AppFont.DEFAULT
        }

        ForceSettings(
            allegiance = allegiance,
            jediLightsaberColor = color,
            appFont = font,
        )
    }

    override suspend fun updateAllegiance(allegiance: ForceAllegiance) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ALLEGIANCE] = allegiance.name
        }
    }

    override suspend fun updateLightsaberColor(color: LightsaberColor) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LIGHTSABER_COLOR] = color.name
        }
    }

    override suspend fun updateAppFont(font: AppFont) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_FONT] = font.name
        }
    }
}
