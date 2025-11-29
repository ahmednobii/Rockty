package com.tutorial.rockty.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tutorial.rockty.ui.detail.LaunchDetailScreen
import com.tutorial.rockty.ui.list.LaunchListScreen
import kotlinx.serialization.Serializable


@Serializable
object LaunchListRoute

@Serializable
data class LaunchDetailRoute(val id: String)

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LaunchListRoute) {
        composable<LaunchListRoute> {
            LaunchListScreen(
                onNavigateToDetail = { launchId ->
                    navController.navigate(LaunchDetailRoute(launchId))
                }
            )
        }
        composable<LaunchDetailRoute> {
            LaunchDetailScreen {
                navController.popBackStack()
            }
        }
    }
}