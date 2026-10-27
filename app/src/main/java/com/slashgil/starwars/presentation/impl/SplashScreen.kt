package com.slashgil.starwars.presentation.impl

import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slashgil.starwars.R
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class Star(val xRatio: Float, val yRatio: Float, val radius: Float, val baseAlpha: Float)

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier,
    bladeColor: Color = Color(0xFF00E5FF),
) {
    val context = LocalContext.current
    val ignitionProgress = remember { Animatable(0f) }

    // Generate static random stars once
    val stars = remember {
        List(120) {
            Star(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat(),
                radius = Random.nextFloat() * 2.5f + 1f,
                baseAlpha = Random.nextFloat() * 0.7f + 0.3f,
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "StarsTwinkle")
    val twinkleFactor by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "StarTwinkleFactor",
    )

    // Play Lightsaber sound effect
    DisposableEffect(Unit) {
        val mediaPlayer = try {
            MediaPlayer.create(context, R.raw.lightsaber).apply {
                start()
            }
        } catch (_: Exception) {
            null
        }

        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    // Trigger Ignition Animation
    LaunchedEffect(Unit) {
        delay(200) // Brief pause before ignition
        ignitionProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
        )
        delay(900) // Hold briefly at full ignition
        onSplashFinished()
    }

    val currentProgress = ignitionProgress.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF030308))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        bladeColor.copy(alpha = (currentProgress * 0.45f).coerceIn(0f, 0.45f)),
                        bladeColor.copy(alpha = (currentProgress * 0.25f).coerceIn(0f, 0.25f)),
                        Color.Transparent,
                    ),
                    radius = 1200f * (0.3f + currentProgress * 0.7f),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        // Starfield Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            stars.forEach { star ->
                val alpha = (star.baseAlpha * twinkleFactor).coerceIn(0f, 1f)
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = star.radius.dp.toPx(),
                    center = Offset(star.xRatio * width, star.yRatio * height),
                )
            }
        }

        // Title and Subtitle Revealing with Blade Ignition
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 110.dp)
                .alpha((currentProgress * 1.5f).coerceIn(0f, 1f)),
        ) {
            Text(
                text = "STAR WARS",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = bladeColor,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "GALAXY ARCHIVES",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = bladeColor,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
            )
        }

        // Lightsaber Canvas (Hilt + Extending Glowing Plasma Blade)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val startY = size.height * 0.82f
            val maxBladeLength = size.height * 0.55f
            val currentBladeLength = maxBladeLength * currentProgress

            if (currentBladeLength > 0f) {
                val bladeTopY = startY - currentBladeLength

                // 1. Outer Plasma Aura
                drawLine(
                    color = bladeColor.copy(alpha = 0.35f * currentProgress),
                    start = Offset(centerX, startY),
                    end = Offset(centerX, bladeTopY),
                    strokeWidth = 64.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                // 2. Inner Neon Energy Aura
                drawLine(
                    color = bladeColor.copy(alpha = 0.85f),
                    start = Offset(centerX, startY),
                    end = Offset(centerX, bladeTopY),
                    strokeWidth = 32.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                // 3. Core Plasma Beam (Pure White)
                drawLine(
                    color = Color.White,
                    start = Offset(centerX, startY),
                    end = Offset(centerX, bladeTopY),
                    strokeWidth = 14.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                // 4. Energy Ignition Flare at Emitter Base
                drawCircle(
                    color = Color.White,
                    radius = (20.dp.toPx() * (0.5f + currentProgress * 0.5f)),
                    center = Offset(centerX, startY),
                )
                drawCircle(
                    color = bladeColor.copy(alpha = 0.6f),
                    radius = (36.dp.toPx() * (0.5f + currentProgress * 0.5f)),
                    center = Offset(centerX, startY),
                )
            }

            // Draw Metallic Lightsaber Hilt
            drawHilt(centerX = centerX, topY = startY)
        }
    }
}

private fun DrawScope.drawHilt(centerX: Float, topY: Float) {
    val hiltWidth = 22.dp.toPx()
    val hiltHeight = 90.dp.toPx()
    val left = centerX - hiltWidth / 2f

    // Main Metallic Body
    drawRoundRect(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF444444),
                Color(0xFFD8D8D8),
                Color(0xFF888888),
                Color(0xFF222222),
            ),
            startX = left,
            endX = left + hiltWidth,
        ),
        topLeft = Offset(left, topY),
        size = Size(hiltWidth, hiltHeight),
        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
    )

    // Emitter Ring (Top Accent)
    drawRect(
        color = Color(0xFF111111),
        topLeft = Offset(left - 2.dp.toPx(), topY),
        size = Size(hiltWidth + 4.dp.toPx(), 10.dp.toPx()),
    )

    // Rubber Grip Grooves (Middle)
    val grooveHeight = 5.dp.toPx()
    val grooveSpacing = 9.dp.toPx()
    val startGrooveY = topY + 30.dp.toPx()
    for (i in 0..4) {
        drawRect(
            color = Color(0xFF151515),
            topLeft = Offset(left + 2.dp.toPx(), startGrooveY + i * grooveSpacing),
            size = Size(hiltWidth - 4.dp.toPx(), grooveHeight),
        )
    }

    // Activation Red Button Accent
    drawCircle(
        color = Color(0xFFFF2202),
        radius = 4.dp.toPx(),
        center = Offset(centerX, topY + 20.dp.toPx()),
    )

    // Pommel Base Cap (Bottom Accent)
    drawRoundRect(
        color = Color(0xFF222222),
        topLeft = Offset(left - 1.dp.toPx(), topY + hiltHeight - 8.dp.toPx()),
        size = Size(hiltWidth + 2.dp.toPx(), 10.dp.toPx()),
        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
    )
}
