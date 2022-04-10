package com.android.readtracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.readtracker.screens.details.BookDetailScreen
import com.android.readtracker.screens.home.Home
import com.android.readtracker.screens.login.TrackerLoginScreen
import com.android.readtracker.screens.search.TrackerSearchScreen
import com.android.readtracker.screens.splash.TrackerSplashScreen
import com.android.readtracker.screens.stats.TrackerStatsScreen
import com.android.readtracker.screens.update.TrackerUpdateScreen


@Composable
fun ReadTrackerNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReadTrackerScreens.SplashScreen.name){

        composable(ReadTrackerScreens.SplashScreen.name){
            TrackerSplashScreen(navController= navController)
        }
        composable(ReadTrackerScreens.ReadTrackerHomeScreen.name){
            Home(navController= navController)
        }
        composable(ReadTrackerScreens.DetailScreen.name){
            BookDetailScreen(navController= navController)
        }
        composable(ReadTrackerScreens.LoginScreen.name){
            TrackerLoginScreen(navController= navController)
        }
        composable(ReadTrackerScreens.SearchScreen.name){
            TrackerSearchScreen(navController= navController)
        }
        composable(ReadTrackerScreens.ReaderStatsScreen.name){
            TrackerStatsScreen(navController= navController)
        }
        composable(ReadTrackerScreens.UpdateScreen.name){
            TrackerUpdateScreen(navController= navController)
        }
    }
}