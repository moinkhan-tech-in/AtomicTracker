package com.challange.atomictracker.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AtomicTrackerTokens(
    val pricePositive: Color,
    val priceNegative: Color,
    val priceNeutral: Color,
)

fun atomicTrackerTokensFrom(colorScheme: ColorScheme): AtomicTrackerTokens =
    AtomicTrackerTokens(
        pricePositive = colorScheme.primary,
        priceNegative = colorScheme.error,
        priceNeutral = colorScheme.onSurfaceVariant,
    )

val LocalAtomicTrackerTokens = staticCompositionLocalOf<AtomicTrackerTokens> {
    error("AtomicTrackerTokens not provided — wrap UI in AtomicTrackerTheme")
}
