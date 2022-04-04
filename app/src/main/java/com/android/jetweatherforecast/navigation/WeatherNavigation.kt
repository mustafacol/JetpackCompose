package com.android.jetweatherforecast.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.jetweatherforecast.screens.MainScreen
import com.android.jetweatherforecast.screens.WeatherSplashScreen
import com.android.jetweatherforecast.screens.main.MainViewModel

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {

        composable(WeatherScreens.SplashScreen.name) {
            WeatherSplashScreen(navController = navController)
        }
        composable(WeatherScreens.SplashScreen.name) {
            val mainViewModel = hiltViewModel<MainViewModel>()
            MainScreen(navController = navController, mainViewModel)
        }

    }
}