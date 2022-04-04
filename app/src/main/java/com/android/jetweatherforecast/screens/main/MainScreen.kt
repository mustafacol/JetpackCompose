package com.android.jetweatherforecast.screens

import android.util.Log
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.jetweatherforecast.data.DataOrException
import com.android.jetweatherforecast.model.Weather
import com.android.jetweatherforecast.screens.main.MainViewModel
import com.android.jetweatherforecast.widgets.WeatherAppBar

@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel) {
    //ShowData(mainViewModel = mainViewModel)
    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true),
    ) {
        value = mainViewModel.getWeatherData("Seattle")
    }.value

    if (weatherData.loading == true) {
        CircularProgressIndicator()
    } else if (weatherData.data != null) {
        MainScaffold(weather = weatherData.data!!, navController = navController)
    }
}

@Composable
fun MainScaffold(weather: Weather, navController: NavController) {
    Scaffold(
        topBar = {
            WeatherAppBar(
                title= weather.city.name+",${weather.city.country}",
                //icon = Icons.Default.ArrowBack,
                elevation = 4.dp
            ){
                Log.d("Back Button","Clicked")
            }
        }
    ) {
        MainContent(data = weather)
    }

}

@Composable
fun MainContent(data: Weather) {
    Text(text = data.city.name)
}