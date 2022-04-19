package com.android.readtracker.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.readtracker.screens.details.BookDetailScreen
import com.android.readtracker.screens.details.BookDetailsViewModel
import com.android.readtracker.screens.home.Home
import com.android.readtracker.screens.home.HomeScreenViewModel
import com.android.readtracker.screens.login.TrackerLoginScreen
import com.android.readtracker.screens.search.BookSearchViewModel
import com.android.readtracker.screens.search.TrackerSearchScreen
import com.android.readtracker.screens.splash.TrackerSplashScreen
import com.android.readtracker.screens.stats.TrackerStatsScreen
import com.android.readtracker.screens.update.TrackerUpdateScreen


@Composable
fun ReadTrackerNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReadTrackerScreens.SplashScreen.name
    ) {

        composable(ReadTrackerScreens.SplashScreen.name) {
            TrackerSplashScreen(navController = navController)
        }
        composable(ReadTrackerScreens.ReadTrackerHomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            Home(navController = navController, viewModel = homeViewModel)
        }

        val detailName = ReadTrackerScreens.DetailScreen.name
        composable("$detailName/{bookId}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.StringType
                }
            )) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailScreen(navController = navController, bookId = it.toString())
            }
            val detailsViewModel = hiltViewModel<BookDetailsViewModel>()
        }
        composable(ReadTrackerScreens.LoginScreen.name) {
            TrackerLoginScreen(navController = navController)
        }
        composable(ReadTrackerScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<BookSearchViewModel>()

            TrackerSearchScreen(navController = navController, viewModel = searchViewModel)
        }
        composable(ReadTrackerScreens.ReaderStatsScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            TrackerStatsScreen(navController = navController, viewModel = homeViewModel)
        }
        val updateScreenName = ReadTrackerScreens.UpdateScreen.name
        composable(
            "$updateScreenName/{bookItemId}",
            arguments = listOf(
                navArgument("bookItemId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("bookItemId").let {
                TrackerUpdateScreen(navController = navController, bookItemId = it.toString())
            }
        }
    }
}