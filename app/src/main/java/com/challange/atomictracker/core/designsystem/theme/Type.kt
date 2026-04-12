package com.challange.atomictracker.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

// Set of Material typography styles to start with
val base = Typography()
val Typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = FontFamily.Monospace),
    displayMedium = base.displayMedium.copy(fontFamily = FontFamily.Monospace),
    displaySmall = base.displaySmall.copy(fontFamily = FontFamily.Monospace),
    headlineLarge = base.headlineLarge.copy(fontFamily = FontFamily.Monospace),
    headlineMedium = base.headlineMedium.copy(fontFamily = FontFamily.Monospace),
    headlineSmall = base.headlineSmall.copy(fontFamily = FontFamily.Monospace),
    titleLarge = base.titleLarge.copy(fontFamily = FontFamily.Monospace),
    titleMedium = base.titleMedium.copy(fontFamily = FontFamily.Monospace),
    titleSmall = base.titleSmall.copy(fontFamily = FontFamily.Monospace),
    bodyLarge = base.bodyLarge.copy(fontFamily = FontFamily.Monospace),
    bodyMedium = base.bodyMedium.copy(fontFamily = FontFamily.Monospace),
    bodySmall = base.bodySmall.copy(fontFamily = FontFamily.Monospace),
    labelLarge = base.labelLarge.copy(fontFamily = FontFamily.Monospace),
    labelMedium = base.labelMedium.copy(fontFamily = FontFamily.Monospace),
    labelSmall = base.labelSmall.copy(fontFamily = FontFamily.Monospace),
)