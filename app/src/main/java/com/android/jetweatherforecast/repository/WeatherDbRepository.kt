package com.android.jetweatherforecast.repository

import com.android.jetweatherforecast.data.WeatherDao
import com.android.jetweatherforecast.model.City
import com.android.jetweatherforecast.model.Favorite
import com.android.jetweatherforecast.model.Unit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDbRepository @Inject constructor(private val weatherDao: WeatherDao) {
    fun getFavorites(): Flow<List<Favorite>> = weatherDao.getFavorites()
    suspend fun insertFavorite(favorite: Favorite) = weatherDao.insertFavorite(favorite = favorite)
    suspend fun updateFavorite(favorite: Favorite) = weatherDao.updateFavorite(favorite = favorite)
    suspend fun deleteAllFavorites() = weatherDao.deleteAllFavorite()
    suspend fun deleteFavorite(favorite: Favorite) = weatherDao.deleteFavorite(favorite = favorite)
    suspend fun getFavById(city: String) = weatherDao.getFavById(city = city)

    fun getUnits(): Flow<List<Unit>> = weatherDao.getUnits()
    suspend fun insertUnits(unit: Unit) = weatherDao.insertUnit(unit = unit)
    suspend fun updateUnit(unit: Unit) = weatherDao.updateUnit(unit = unit)
    suspend fun deleteUnit(unit: Unit) = weatherDao.deleteUnit(unit = unit)
    suspend fun deleteAllUnits() = weatherDao.deleteAllUnits()

}