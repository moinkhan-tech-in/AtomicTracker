package com.challange.atomictracker.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.challange.atomictracker.feature.detail.DetailScreen
import com.challange.atomictracker.feature.feed.FeedScreen

@Composable
fun AtomicTrackerNavHost(
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
                onOpenDetail = { id -> navController.navigate(DetailRoute(id = id)) },
            )
        }
        composable<DetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DetailRoute>()
            DetailScreen(
                id = route.id,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
