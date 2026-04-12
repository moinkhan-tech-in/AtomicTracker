package com.challange.atomictracker.core.designsystem.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.challange.atomictracker.core.navigation.AtomicTrackerNavHost

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun AtomicTrackerRoot() {
    var themeModeName by rememberSaveable { mutableStateOf(ThemeMode.FollowSystem.name) }
    val themeMode = ThemeMode.valueOf(themeModeName)
    AtomicTrackerTheme(themeMode = themeMode) {
        AtomicTrackerNavHost(
            themeMode = themeMode,
            onThemeModeChange = { themeModeName = it.name },
        )
    }
}

@Composable
fun AtomicTrackerTheme(
    themeMode: ThemeMode = ThemeMode.FollowSystem,
    content: @Composable () -> Unit
) {

    val darkTheme = themeMode.resolveDark(isSystemInDarkTheme())
    val fraction by animateFloatAsState(
        targetValue = if (darkTheme) 1f else 0f,
        animationSpec = tween(durationMillis = 2000),
        label = "ThemeTransition"
    )
    val colorScheme = lerpColorScheme(LightColorScheme, DarkColorScheme, fraction)
    val trackerColorScheme = lerpAtomicTrackerColorScheme(LightAtomicTrackerColorScheme, DarkAtomicTrackerColorScheme, fraction)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = view.context.findActivity()?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        CompositionLocalProvider(LocalAtomicTrackerColorScheme provides trackerColorScheme) {
            content()
        }
    }
}