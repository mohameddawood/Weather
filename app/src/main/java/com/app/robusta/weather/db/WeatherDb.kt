package com.app.robusta.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.robusta.weather.db.dao.WeatherDao
import com.app.robusta.weather.db.entities.HistoryWeatherItem
import com.app.robusta.weather.uitls.Converters

@Database(entities = [ HistoryWeatherItem::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDb : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}