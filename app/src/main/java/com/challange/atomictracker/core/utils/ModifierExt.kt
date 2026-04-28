package com.challange.atomictracker.core.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Pulses alpha between [minAlpha] and [maxAlpha] while [enabled] is true.
 * When disabled, the modifier is a no-op so content stays fully opaque.
 */
fun Modifier.blink(
    enabled: Boolean,
    minAlpha: Float = 0.5f,
    maxAlpha: Float = 1f,
    durationMillis: Int = 800,
): Modifier = composed {
    if (!enabled) {
        this
    } else {
        val transition = rememberInfiniteTransition(label = "blink")
        val alpha by transition.animateFloat(
            initialValue = minAlpha,
            targetValue = maxAlpha,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "blinkAlpha",
        )
        graphicsLayer { this.alpha = alpha }
    }
}
