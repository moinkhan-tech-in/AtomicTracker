package com.challange.atomictracker.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AtomicTrackerColorScheme(
    val positive: Color,
    val negative: Color,
    val neutral: Color,
)

val LightAtomicTrackerColorScheme = AtomicTrackerColorScheme(
    positive = SemanticPositiveLight,
    negative = SemanticNegativeLight,
    neutral = Color.Unspecified,
)

val DarkAtomicTrackerColorScheme = AtomicTrackerColorScheme(
    positive = SemanticPositiveDark,
    negative = SemanticNegativeDark,
    neutral = Color.Unspecified
)

val LocalAtomicTrackerColorScheme = staticCompositionLocalOf<AtomicTrackerColorScheme> {
    error("AtomicTrackerTokens not provided — wrap UI in AtomicTrackerTheme")
}
