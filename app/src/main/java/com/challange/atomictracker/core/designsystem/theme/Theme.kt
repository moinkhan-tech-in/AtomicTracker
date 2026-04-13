package com.challange.atomictracker.core.designsystem.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.challange.atomictracker.R
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
fun AtomicTrackerRoot(isOnline: Boolean) {
    var themeModeName by rememberSaveable { mutableStateOf(ThemeMode.FollowSystem.name) }
    val themeMode = ThemeMode.valueOf(themeModeName)
    val snackbarHostState = remember { SnackbarHostState() }
    val message = stringResource(R.string.snackbar_no_internet)
    LaunchedEffect(isOnline) {
        if (isOnline) {
            snackbarHostState.currentSnackbarData?.dismiss()
        } else {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Indefinite)
        }
    }

    AtomicTrackerTheme(themeMode = themeMode) {
        Box(Modifier.fillMaxSize()) {
            AtomicTrackerNavHost(
                themeMode = themeMode,
                onThemeModeChange = { themeModeName = it.name },
            )
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter),
            )
        }
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