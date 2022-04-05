package com.android.jetweatherforecast.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.jetweatherforecast.screens.MainScreen
import com.android.jetweatherforecast.screens.SettingsScreen
import com.android.jetweatherforecast.screens.WeatherSplashScreen
import com.android.jetweatherforecast.screens.about.AboutScreen
import com.android.jetweatherforecast.screens.favorite.FavoriteScreen
import com.android.jetweatherforecast.screens.main.MainViewModel
import com.android.jetweatherforecast.screens.search.SearchScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {

        composable(WeatherScreens.SplashScreen.name) {
            WeatherSplashScreen(navController = navController)
        }
        val route = WeatherScreens.MainScreen.name
        composable("$route/{city}",
            arguments = listOf(
                navArgument(name = "city") {
                    type = NavType.StringType
                }
            )
        ) { navBack ->
            navBack.arguments?.getString("city").let { city ->
                val mainViewModel = hiltViewModel<MainViewModel>()
                MainScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    city = city
                )
            }

        }
        composable(WeatherScreens.SearchScreen.name) {
            //val mainViewModel = hiltViewModel<MainViewModel>()
            SearchScreen(navController = navController)
        }
        composable(WeatherScreens.AboutScreen.name) {
            //val mainViewModel = hiltViewModel<MainViewModel>()
            AboutScreen(navController = navController)
        }
        composable(WeatherScreens.FavoriteScreen.name) {
            //val mainViewModel = hiltViewModel<MainViewModel>()
            FavoriteScreen(navController = navController)
        }
        composable(WeatherScreens.SettingsScreen.name) {
            //val mainViewModel = hiltViewModel<MainViewModel>()
            SettingsScreen(navController = navController)
        }

    }
}