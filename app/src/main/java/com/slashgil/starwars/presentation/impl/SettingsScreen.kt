package com.slashgil.starwars.presentation.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slashgil.starwars.domain.contract.AppFont
import com.slashgil.starwars.domain.contract.ForceAllegiance
import com.slashgil.starwars.domain.contract.ForceSettings
import com.slashgil.starwars.domain.contract.LightsaberColor
import com.slashgil.starwars.ui.theme.StarWarsBlack
import com.slashgil.starwars.ui.theme.StarWarsCardBorder
import com.slashgil.starwars.ui.theme.StarWarsDarkGray
import com.slashgil.starwars.ui.theme.StarWarsTextPrimary
import com.slashgil.starwars.ui.theme.StarWarsTextSecondary
import com.slashgil.starwars.ui.theme.StarWarsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    forceSettings: ForceSettings,
    onSetAllegiance: (ForceAllegiance) -> Unit,
    onSetLightsaberColor: (LightsaberColor) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onSetAppFont: (AppFont) -> Unit = {},
) {
    val accentColor = forceSettings.accentColor
    var showFontSelection by remember { mutableStateOf(forceSettings.appFont != AppFont.DEFAULT) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StarWarsBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FORCE CONFIGURATION",
                        style = StarWarsTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Hub",
                            tint = accentColor,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StarWarsBlack,
                    titleContentColor = accentColor,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Interactive Lightsaber Blade Preview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StarWarsDarkGray),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.6f)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "ACTIVE KYBER ENERGY",
                        style = StarWarsTheme.typography.labelLarge,
                        color = StarWarsTextSecondary,
                        letterSpacing = 2.sp,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                    ) {
                        val centerX = size.width / 2f
                        val startY = size.height * 0.85f
                        val topY = size.height * 0.12f

                        // Outer Glow
                        drawLine(
                            color = accentColor.copy(alpha = 0.35f),
                            start = Offset(centerX, startY),
                            end = Offset(centerX, topY),
                            strokeWidth = 36.dp.toPx(),
                            cap = StrokeCap.Round,
                        )
                        // Inner Glow
                        drawLine(
                            color = accentColor,
                            start = Offset(centerX, startY),
                            end = Offset(centerX, topY),
                            strokeWidth = 20.dp.toPx(),
                            cap = StrokeCap.Round,
                        )
                        // Pure Core
                        drawLine(
                            color = Color.White,
                            start = Offset(centerX, startY),
                            end = Offset(centerX, topY),
                            strokeWidth = 8.dp.toPx(),
                            cap = StrokeCap.Round,
                        )

                        // Mini Hilt
                        drawMiniHilt(centerX = centerX, topY = startY)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (forceSettings.allegiance == ForceAllegiance.SITH) {
                            "DARK SIDE • SITH CRIMSON"
                        } else {
                            "LIGHT SIDE • ${forceSettings.jediLightsaberColor.displayName.uppercase()}"
                        },
                        style = StarWarsTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        letterSpacing = 1.sp,
                    )
                }
            }

            // Allegiance Selection Header
            Text(
                text = "CHOOSE YOUR PATH",
                style = StarWarsTheme.typography.titleSmall,
                color = StarWarsTextSecondary,
                letterSpacing = 1.5.sp,
            )

            // Sith Selection Card ("Are you a Sith?")
            val isSithSelected = forceSettings.allegiance == ForceAllegiance.SITH
            val sithRed = Color(0xFFEB212E)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSetAllegiance(ForceAllegiance.SITH) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSithSelected) Color(0xFF200508) else StarWarsDarkGray,
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = if (isSithSelected) 2.dp else 1.dp,
                    color = if (isSithSelected) sithRed else StarWarsCardBorder,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = isSithSelected,
                        onClick = { onSetAllegiance(ForceAllegiance.SITH) },
                        colors = RadioButtonDefaults.colors(selectedColor = sithRed),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Are you a Sith?",
                            style = StarWarsTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSithSelected) sithRed else StarWarsTextPrimary,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Embrace the dark side. Sets all app accents and lightsaber energy to crimson red.",
                            style = StarWarsTheme.typography.bodyMedium,
                            color = StarWarsTextSecondary,
                        )
                    }
                }
            }

            // Jedi Selection Card ("Are you a Jedi?")
            val isJediSelected = forceSettings.allegiance == ForceAllegiance.JEDI
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSetAllegiance(ForceAllegiance.JEDI) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isJediSelected) Color(0xFF04121A) else StarWarsDarkGray,
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = if (isJediSelected) 2.dp else 1.dp,
                    color = if (isJediSelected) forceSettings.jediLightsaberColor.color else StarWarsCardBorder,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = isJediSelected,
                        onClick = { onSetAllegiance(ForceAllegiance.JEDI) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = forceSettings.jediLightsaberColor.color,
                        ),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Are you a Jedi?",
                            style = StarWarsTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isJediSelected) forceSettings.jediLightsaberColor.color else StarWarsTextPrimary,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Protect peace and justice in the galaxy. Choose your Kyber Crystal color below.",
                            style = StarWarsTheme.typography.bodyMedium,
                            color = StarWarsTextSecondary,
                        )
                    }
                }
            }

            // Kyber Crystal Color Selection Grid (Visible when Jedi is selected)
            AnimatedVisibility(
                visible = isJediSelected,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "JEDI KYBER CRYSTAL COLOR",
                        style = StarWarsTheme.typography.titleSmall,
                        color = StarWarsTextSecondary,
                        letterSpacing = 1.5.sp,
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        LightsaberColor.entries.forEach { colorOption ->
                            val isColorSelected = forceSettings.jediLightsaberColor == colorOption
                            val chipBorderColor = if (isColorSelected) colorOption.color else StarWarsCardBorder

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { onSetLightsaberColor(colorOption) },
                                color = if (isColorSelected) colorOption.color.copy(alpha = 0.2f) else StarWarsDarkGray,
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.5.dp, chipBorderColor),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(colorOption.color, CircleShape)
                                            .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = colorOption.displayName,
                                        style = StarWarsTheme.typography.labelLarge,
                                        fontWeight = if (isColorSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isColorSelected) colorOption.color else StarWarsTextPrimary,
                                    )
                                    if (isColorSelected) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = colorOption.color,
                                            modifier = Modifier.size(16.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Question Section: "Do you want a different font?"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val newValue = !showFontSelection
                        showFontSelection = newValue
                        if (!newValue) {
                            onSetAppFont(AppFont.DEFAULT)
                        }
                    },
                colors = CardDefaults.cardColors(containerColor = StarWarsDarkGray),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = if (showFontSelection) 1.5.dp else 1.dp,
                    color = if (showFontSelection) accentColor else StarWarsCardBorder,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = Icons.Default.FontDownload,
                            contentDescription = "Typography icon",
                            tint = accentColor,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Do you want a different font?",
                                style = StarWarsTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = StarWarsTextPrimary,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Customize the font style across the entire archive.",
                                style = StarWarsTheme.typography.bodyMedium,
                                color = StarWarsTextSecondary,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = showFontSelection,
                        onCheckedChange = { checked ->
                            showFontSelection = checked
                            if (!checked) {
                                onSetAppFont(AppFont.DEFAULT)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = accentColor,
                            uncheckedThumbColor = StarWarsTextSecondary,
                            uncheckedTrackColor = StarWarsBlack,
                        ),
                    )
                }
            }

            // Galactic Typography Selection Grid & Live Font Preview
            AnimatedVisibility(
                visible = showFontSelection,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = "GALACTIC TYPOGRAPHY STYLE",
                        style = StarWarsTheme.typography.titleSmall,
                        color = StarWarsTextSecondary,
                        letterSpacing = 1.5.sp,
                    )

                    // Font Live Preview Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = StarWarsBlack),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "STAR WARS ARCHIVES",
                                style = StarWarsTheme.typography.titleLarge,
                                color = accentColor,
                                letterSpacing = 2.sp,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "May the Force be with you",
                                style = StarWarsTheme.typography.bodyMedium,
                                color = StarWarsTextPrimary,
                            )
                        }
                    }

                    // Font Choice Chips
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        AppFont.entries.forEach { fontOption ->
                            val isFontSelected = forceSettings.appFont == fontOption
                            val chipBorderColor = if (isFontSelected) accentColor else StarWarsCardBorder

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { onSetAppFont(fontOption) },
                                color = if (isFontSelected) accentColor.copy(alpha = 0.2f) else StarWarsDarkGray,
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.5.dp, chipBorderColor),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = fontOption.displayName,
                                        style = StarWarsTheme.typography.labelLarge.copy(
                                            fontFamily = fontOption.fontFamily,
                                        ),
                                        fontWeight = if (isFontSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isFontSelected) accentColor else StarWarsTextPrimary,
                                    )
                                    if (isFontSelected) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected font",
                                            tint = accentColor,
                                            modifier = Modifier.size(16.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun DrawScope.drawMiniHilt(centerX: Float, topY: Float) {
    val hiltWidth = 14.dp.toPx()
    val hiltHeight = 35.dp.toPx()
    val left = centerX - hiltWidth / 2f

    drawRoundRect(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF555555),
                Color(0xFFCCCCCC),
                Color(0xFF777777),
                Color(0xFF222222),
            ),
            startX = left,
            endX = left + hiltWidth,
        ),
        topLeft = Offset(left, topY),
        size = Size(hiltWidth, hiltHeight),
        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
    )
}
