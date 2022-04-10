package com.android.jetweatherforecast.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.jetweatherforecast.data.DataOrException
import com.android.jetweatherforecast.model.Weather
import com.android.jetweatherforecast.model.WeatherItem
import com.android.jetweatherforecast.navigation.WeatherScreens
import com.android.jetweatherforecast.screens.favorite.FavoriteViewModel
import com.android.jetweatherforecast.screens.main.MainViewModel
import com.android.jetweatherforecast.screens.settings.SettingsViewModel
import com.android.jetweatherforecast.utils.formatDate
import com.android.jetweatherforecast.utils.formatDecimals
import com.android.jetweatherforecast.widgets.*

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    city: String?,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    //ShowData(mainViewModel = mainViewModel)
    val curCity: String = if (city.isNullOrEmpty()) "Seattle" else city
    val unitFromDb = settingsViewModel.unitList.collectAsState().value
    var unit by remember {
        mutableStateOf("imperial")
    }
    var isImperial by remember {
        mutableStateOf(false)
    }

    if (!unitFromDb.isNullOrEmpty()) {
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()
        isImperial = unit == "imperial"

        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true),
        ) {
            value = mainViewModel.getWeatherData(city = curCity, unit = unit)
        }.value

        if (weatherData.loading == true) {
            CircularProgressIndicator()
        } else if (weatherData.data != null) {
            MainScaffold(
                weather = weatherData.data!!,
                navController = navController,
                isImperial = isImperial
            )
        }

    }


}

@Composable
fun MainScaffold(weather: Weather, navController: NavController, isImperial: Boolean) {
    Scaffold(
        topBar = {
            WeatherAppBar(
                title = weather.city.name + ",${weather.city.country}",
                //icon = Icons.Default.ArrowBack,
                elevation = 4.dp,
                navController = navController,
                onAddActionClicked = {
                    navController.navigate(WeatherScreens.SearchScreen.name)
                }
            ) {
                Log.d("Back Button", "Clicked")
            }
        }
    ) {
        MainContent(data = weather, isImperial = isImperial)
    }

}

@Composable
fun MainContent(data: Weather, isImperial: Boolean) {
    val imageUrl = "https://openweathermap.org/img/wn/${data.list[0].weather[0].icon}.png"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formatDate(data.list[0].dt),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSecondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp
        )
        Surface(
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp),
            shape = CircleShape,
            color = Color(0xFFFDBA29)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(
                    text = formatDecimals(data.list[0].temp.day) + "°",
                    style = MaterialTheme.typography.h4, fontWeight = FontWeight.SemiBold
                )
                Text(text = data.list[0].weather[0].main, fontStyle = FontStyle.Italic)
            }
        }

        HumidityWindPressureRow(weather = data.list[0], isImperial = isImperial)
        Divider()
        SunSetSunRiseRow(weather = data.list[0])
        Text(text = "This Week", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            color = Color(0xFFEEF1EF),
            shape = RoundedCornerShape(14.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(2.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
                items(items = data.list) { item: WeatherItem ->
                    WeeklyDetailRow(weatherItem = item)
                }
            }
        }
    }
}

