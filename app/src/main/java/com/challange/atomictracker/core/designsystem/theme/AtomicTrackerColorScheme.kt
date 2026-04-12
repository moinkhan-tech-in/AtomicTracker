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
    positive = Color(0xFF16A34A), // deeper green
    negative = Color(0xFFDC2626), // deeper red
    neutral = Color(0xFF6B7280)   // darker gray
)

val DarkAtomicTrackerColorScheme = AtomicTrackerColorScheme(
    positive = Color(0xFF4ADE80), // softer bright green
    negative = Color(0xFFF87171), // softer red
    neutral = Color(0xFF9CA3AF)   // gray
)

val LocalAtomicTrackerColorScheme = staticCompositionLocalOf<AtomicTrackerColorScheme> {
    error("AtomicTrackerTokens not provided — wrap UI in AtomicTrackerTheme")
}
