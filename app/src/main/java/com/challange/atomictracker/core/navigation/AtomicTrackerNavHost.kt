package com.challange.atomictracker.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.challange.atomictracker.core.designsystem.theme.ThemeMode
import com.challange.atomictracker.feature.detail.DetailScreen
import com.challange.atomictracker.feature.feed.FeedScreen

@Composable
fun AtomicTrackerNavHost(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = FeedRoute,
        modifier = modifier.fillMaxSize(),
    ) {
        composable<FeedRoute> {
            FeedScreen(
                onOpenDetail = { symbol -> navController.navigate(DetailRoute(symbol = symbol)) },
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange
            )
        }
        composable<DetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DetailRoute>()
            DetailScreen(
                symbol = route.symbol,
                onBack = { navController.popBackStack() },
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange
            )
        }
    }
}
