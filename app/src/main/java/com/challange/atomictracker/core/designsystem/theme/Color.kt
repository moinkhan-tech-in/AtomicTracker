package com.challange.atomictracker.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),      // Calm blue (actions)
    secondary = Color(0xFF22C55E),    // Profit hint
    tertiary = Color(0xFFF59E0B),     // Accent (warning/neutral highlight)

    background = Color(0xFF0B0F19),   // Deep navy (not pure black)
    surface = Color(0xFF121826),      // Card background
    surfaceVariant = Color(0xFF1E293B),

    onPrimary = Color.Black,
    onBackground = Color(0xFFE5E7EB),
    onSurface = Color(0xFFE5E7EB),

    error = Color(0xFFEF4444)         // Loss
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2563EB),      // Strong blue
    secondary = Color(0xFF16A34A),    // Profit
    tertiary = Color(0xFFD97706),     // Accent

    background = Color(0xFFF8FAFC),   // Soft white (not harsh)
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE2E8F0),

    onPrimary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),

    error = Color(0xFFDC2626)         // Loss
)


fun lerpAtomicTrackerColorScheme(
    from: AtomicTrackerColorScheme,
    to: AtomicTrackerColorScheme,
    fraction: Float
) = from.copy(
    neutral = lerp(from.neutral, to.neutral, fraction)
)

fun lerpColorScheme(from: ColorScheme, to: ColorScheme, fraction: Float): ColorScheme =
    from.copy(
        primary = lerp(from.primary, to.primary, fraction),
        onPrimary = lerp(from.onPrimary, to.onPrimary, fraction),
        primaryContainer = lerp(from.primaryContainer, to.primaryContainer, fraction),
        onPrimaryContainer = lerp(from.onPrimaryContainer, to.onPrimaryContainer, fraction),
        secondary = lerp(from.secondary, to.secondary, fraction),
        onSecondary = lerp(from.onSecondary, to.onSecondary, fraction),
        secondaryContainer = lerp(from.secondaryContainer, to.secondaryContainer, fraction),
        onSecondaryContainer = lerp(from.onSecondaryContainer, to.onSecondaryContainer, fraction),
        tertiary = lerp(from.tertiary, to.tertiary, fraction),
        onTertiary = lerp(from.onTertiary, to.onTertiary, fraction),
        tertiaryContainer = lerp(from.tertiaryContainer, to.tertiaryContainer, fraction),
        onTertiaryContainer = lerp(from.onTertiaryContainer, to.onTertiaryContainer, fraction),
        error = lerp(from.error, to.error, fraction),
        onError = lerp(from.onError, to.onError, fraction),
        errorContainer = lerp(from.errorContainer, to.errorContainer, fraction),
        onErrorContainer = lerp(from.onErrorContainer, to.onErrorContainer, fraction),
        background = lerp(from.background, to.background, fraction),
        onBackground = lerp(from.onBackground, to.onBackground, fraction),
        surface = lerp(from.surface, to.surface, fraction),
        onSurface = lerp(from.onSurface, to.onSurface, fraction),
        surfaceContainerLowest = lerp(
            from.surfaceContainerLowest,
            to.surfaceContainerLowest,
            fraction
        ),
        surfaceContainerLow = lerp(from.surfaceContainerLow, to.surfaceContainerLow, fraction),
        surfaceContainer = lerp(from.surfaceContainer, to.surfaceContainer, fraction),
        surfaceContainerHigh = lerp(from.surfaceContainerHigh, to.surfaceContainerHigh, fraction),
        surfaceContainerHighest = lerp(
            from.surfaceContainerHighest,
            to.surfaceContainerHighest,
            fraction
        ),
        surfaceVariant = lerp(from.surfaceVariant, to.surfaceVariant, fraction),
        onSurfaceVariant = lerp(from.onSurfaceVariant, to.onSurfaceVariant, fraction),
        surfaceTint = lerp(from.surfaceTint, to.surfaceTint, fraction),
        outline = lerp(from.outline, to.outline, fraction),
        outlineVariant = lerp(from.outlineVariant, to.outlineVariant, fraction),
        scrim = lerp(from.scrim, to.scrim, fraction),
        inverseSurface = lerp(from.inverseSurface, to.inverseSurface, fraction),
        inverseOnSurface = lerp(from.inverseOnSurface, to.inverseOnSurface, fraction),
        inversePrimary = lerp(from.inversePrimary, to.inversePrimary, fraction)
    )