package com.slashgil.starwars.data.contract

import com.slashgil.starwars.domain.contract.AppFont
import com.slashgil.starwars.domain.contract.ForceAllegiance
import com.slashgil.starwars.domain.contract.ForceSettings
import com.slashgil.starwars.domain.contract.LightsaberColor
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val forceSettingsFlow: Flow<ForceSettings>
    suspend fun updateAllegiance(allegiance: ForceAllegiance)
    suspend fun updateLightsaberColor(color: LightsaberColor)
    suspend fun updateAppFont(font: AppFont)
}
