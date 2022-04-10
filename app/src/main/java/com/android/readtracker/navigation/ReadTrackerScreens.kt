package com.android.readtracker.navigation

import java.lang.IllegalArgumentException

enum class ReadTrackerScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    ReadTrackerHomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    ReaderStatsScreen;

    companion object{
        fun fromRoute(route:String): ReadTrackerScreens =
            when(route.substringBefore("/")){
                SplashScreen.name -> SplashScreen
                LoginScreen.name -> LoginScreen
                CreateAccountScreen.name -> CreateAccountScreen
                ReadTrackerHomeScreen.name -> ReadTrackerHomeScreen
                SearchScreen.name -> SearchScreen
                DetailScreen.name -> DetailScreen
                UpdateScreen.name -> UpdateScreen
                ReaderStatsScreen.name -> ReaderStatsScreen
                null -> ReadTrackerHomeScreen
                else -> throw IllegalArgumentException("Route is not recognized.")
            }
    }

}