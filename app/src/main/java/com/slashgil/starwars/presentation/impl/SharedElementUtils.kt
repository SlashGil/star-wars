package com.slashgil.starwars.presentation.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.safeSharedElement(
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?,
    key: String,
    enabled: Boolean = true
): Modifier {
    if (!enabled || sharedTransitionScope == null || animatedVisibilityScope == null) {
        return this
    }
    val sharedContentState = with(sharedTransitionScope) {
        rememberSharedContentState(key = key)
    }
    return try {
        with(sharedTransitionScope) {
            this@safeSharedElement.sharedElement(
                sharedContentState = sharedContentState,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    } catch (t: Throwable) {
        this
    }
}
