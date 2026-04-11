package com.challange.atomictracker.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AtomicTrackerTokens(
    val positive: Color,
    val negative: Color,
    val neutral: Color,
)

fun atomicTrackerTokensFrom(colorScheme: ColorScheme, isDarkTheme: Boolean): AtomicTrackerTokens =
    AtomicTrackerTokens(
        positive = if (isDarkTheme) SemanticPositiveDark else SemanticPositiveLight,
        negative = if (isDarkTheme) SemanticNegativeDark else SemanticNegativeLight,
        neutral = colorScheme.onSurfaceVariant,
    )

val LocalAtomicTrackerTokens = staticCompositionLocalOf<AtomicTrackerTokens> {
    error("AtomicTrackerTokens not provided — wrap UI in AtomicTrackerTheme")
}
