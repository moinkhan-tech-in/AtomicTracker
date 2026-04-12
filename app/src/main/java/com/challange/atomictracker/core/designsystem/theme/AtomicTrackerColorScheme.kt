package com.challange.atomictracker.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


@Immutable
data class AtomicTrackerColorScheme(
    val pricePositive: Color,
    val priceNegative: Color,
    val priceNeutral: Color
)


val LightAtomicTrackerColorScheme = AtomicTrackerColorScheme(
    pricePositive = Color(0xFF16A34A), // deeper green
    priceNegative = Color(0xFFDC2626), // deeper red
    priceNeutral = Color(0xFF6B7280)   // darker gray
)

val DarkAtomicTrackerColorScheme = AtomicTrackerColorScheme(
    pricePositive = Color(0xFF4ADE80), // softer bright green
    priceNegative = Color(0xFFF87171), // softer red
    priceNeutral = Color(0xFF9CA3AF)   // gray
)

val LocalAtomicTrackerColorScheme = staticCompositionLocalOf<AtomicTrackerColorScheme> {
    error("AtomicTrackerTokens not provided — wrap UI in AtomicTrackerTheme")
}
