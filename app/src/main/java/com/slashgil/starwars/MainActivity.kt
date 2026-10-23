package com.slashgil.starwars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.slashgil.starwars.presentation.contract.StarWarsApp
import com.slashgil.starwars.presentation.impl.SplashScreen
import com.slashgil.starwars.presentation.impl.StarWarsViewModel
import com.slashgil.starwars.ui.theme.StarWarsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: StarWarsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            StarWarsTheme(forceSettings = uiState.forceSettings) {
                var showSplash by remember { mutableStateOf(true) }

                Crossfade(
                    targetState = showSplash,
                    animationSpec = tween(durationMillis = 800),
                    label = "SplashCrossfade",
                ) { isSplashVisible ->
                    if (isSplashVisible) {
                        SplashScreen(
                            onSplashFinished = { showSplash = false },
                            bladeColor = uiState.accentColor,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        StarWarsApp(
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}
