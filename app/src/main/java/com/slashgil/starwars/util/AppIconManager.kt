package com.slashgil.starwars.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.slashgil.starwars.domain.contract.ForceAllegiance
import com.slashgil.starwars.domain.contract.ForceSettings

object AppIconManager {

    private const val PACKAGE_NAME = "com.slashgil.starwars"

    val ALIAS_MAP = mapOf(
        "DEFAULT" to "$PACKAGE_NAME.LauncherAliasDefault",
        "SITH" to "$PACKAGE_NAME.LauncherAliasSith",
        "BLUE" to "$PACKAGE_NAME.LauncherAliasJediBlue",
        "GREEN" to "$PACKAGE_NAME.LauncherAliasJediGreen",
        "PURPLE" to "$PACKAGE_NAME.LauncherAliasJediPurple",
        "YELLOW" to "$PACKAGE_NAME.LauncherAliasJediYellow",
        "CYAN" to "$PACKAGE_NAME.LauncherAliasJediCyan",
        "MAGENTA" to "$PACKAGE_NAME.LauncherAliasJediMagenta",
        "WHITE" to "$PACKAGE_NAME.LauncherAliasJediWhite",
    )

    fun updateIcon(context: Context, forceSettings: ForceSettings) {
        val activeKey = when (forceSettings.allegiance) {
            ForceAllegiance.SITH -> "SITH"
            ForceAllegiance.JEDI -> forceSettings.jediLightsaberColor.name
        }

        val targetAlias = ALIAS_MAP[activeKey] ?: ALIAS_MAP["DEFAULT"]!!
        val packageManager = context.packageManager

        ALIAS_MAP.values.forEach { aliasName ->
            val state = if (aliasName == targetAlias) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }

            val componentName = ComponentName(context, aliasName)
            try {
                if (packageManager.getComponentEnabledSetting(componentName) != state) {
                    packageManager.setComponentEnabledSetting(
                        componentName,
                        state,
                        PackageManager.DONT_KILL_APP,
                    )
                }
            } catch (_: Exception) {
                // Ignore component state errors if alias is not registered
            }
        }
    }
}
