package com.example.ammoramusicapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ammoramusicapp.ui.detail.DetailScreen
import com.example.ammoramusicapp.ui.home.HomeScreen
import com.example.ammoramusicapp.data.model.Album

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Detail : Screens("detail/{album}") {
        fun createRoute(album: Album): String = "detail/${album.id}"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) {
            HomeScreen(navController)
        }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            DetailScreen(navController, id)
        }
    }
}
