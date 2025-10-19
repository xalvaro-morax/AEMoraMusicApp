package com.example.ammoramusicapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ammoramusicapp.ui.detail.DetailScreen
import com.example.ammoramusicapp.ui.home.HomeScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }

        composable("detail/{id}") { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(navController = navController, albumId = albumId)
        }
    }
}
