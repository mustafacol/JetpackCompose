package com.android.jetweatherforecast.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.jetweatherforecast.data.DataOrException
import com.android.jetweatherforecast.model.City
import com.android.jetweatherforecast.model.Weather
import com.android.jetweatherforecast.model.WeatherObject
import com.android.jetweatherforecast.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {
    val data: MutableState<DataOrException<Weather, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    suspend fun getWeatherData(city: String):DataOrException<Weather,Boolean,Exception>{
        return repository.getWeather(city)
    }



}