package com.example.remedialucp2_235.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController // Pastikan ini ter-import
import com.example.remedialucp2_235.ui.view.BookFormScreen
import com.example.remedialucp2_235.ui.view.HomeScreen

enum class Destinasi {
    Home,
    Entry
}

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destinasi.Home.name,
        modifier = modifier
    ) {
        composable(Destinasi.Home.name) {
            HomeScreen(
                navigateToEntry = { navController.navigate(Destinasi.Entry.name) }
            )
        }
        composable(Destinasi.Entry.name) {
            BookFormScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}