package com.android.jetweatherforecast.repository

import com.android.jetweatherforecast.data.DataOrException
import com.android.jetweatherforecast.model.Unit
import com.android.jetweatherforecast.model.Weather
import com.android.jetweatherforecast.model.WeatherObject
import com.android.jetweatherforecast.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {

    suspend fun getWeather(
        cityQuery: String,
        unit: String
    ): DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeather(query = cityQuery, units = unit)
        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }
}