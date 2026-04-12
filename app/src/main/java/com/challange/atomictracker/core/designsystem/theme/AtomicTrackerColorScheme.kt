package com.challange.atomictracker.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val SemanticPositiveLight = Color(0xFF1B5E20)
val SemanticPositiveDark = Color(0xFF69F0AE)
val SemanticNegativeLight = Color(0xFFB71C1C)
val SemanticNegativeDark = Color(0xFFFF8A80)

val SemanticNeutralLight = Color(0xFF546E7A)
val SemanticNeutralDark = Color(0xFFB0BEC5)

@Immutable
data class AtomicTrackerColorScheme(
    val positive: Color,
    val negative: Color,
    val neutral: Color,
)

val LightAtomicTrackerColorScheme = AtomicTrackerColorScheme(
    positive = SemanticPositiveLight,
    negative = SemanticNegativeLight,
    neutral = SemanticNeutralLight,
)

val DarkAtomicTrackerColorScheme = AtomicTrackerColorScheme(
    positive = SemanticPositiveDark,
    negative = SemanticNegativeDark,
    neutral = SemanticNeutralDark,
)

val LocalAtomicTrackerColorScheme = staticCompositionLocalOf<AtomicTrackerColorScheme> {
    error("AtomicTrackerTokens not provided — wrap UI in AtomicTrackerTheme")
}
