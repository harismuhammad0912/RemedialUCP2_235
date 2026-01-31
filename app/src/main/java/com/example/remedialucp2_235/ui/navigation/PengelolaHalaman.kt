package com.example.remedialucp2_235.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.remedialucp2_235.ui.view.BookFormScreen
import com.example.remedialucp2_235.ui.view.HomeScreen
import com.example.remedialucp2_235.ui.view.AuthorScreen
import com.example.remedialucp2_235.ui.view.BookListScreen
import com.example.remedialucp2_235.ui.viewmodel.LibraryViewModel
import com.example.remedialucp2_235.ui.viewmodel.PenyediaViewModel

enum class Destinasi {
    Home,
    Entry,
    Author,
    BookList
}

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    NavHost(
        navController = navController,
        startDestination = Destinasi.Home.name,
        modifier = modifier
    ) {
        composable(Destinasi.Home.name) {
            HomeScreen(
                navigateToEntry = {
                    viewModel.resetForm() // Reset form sebelum masuk
                    navController.navigate(Destinasi.Entry.name)
                },
                navigateToAuthors = {
                    viewModel.resetForm()
                    navController.navigate(Destinasi.Author.name)
                },
                navigateToBooks = {
                    navController.navigate(Destinasi.BookList.name)
                }
            )
        }

        composable(Destinasi.Entry.name) {
            BookFormScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(Destinasi.Author.name) {
            AuthorScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(Destinasi.BookList.name) {
            BookListScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}