package com.android.jetweatherforecast.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.jetweatherforecast.model.Favorite
import com.android.jetweatherforecast.model.Unit

@Database(entities = [Favorite::class, Unit::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}