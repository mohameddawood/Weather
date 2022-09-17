package com.app.robusta.weather.db.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import com.app.robusta.weather.db.entities.HistoryWeatherItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Dao
interface WeatherDao {
    @Insert
     suspend fun addNewWeatherStatusToHistory(item: HistoryWeatherItem)

    @Query("select * from HistoryWeatherItem")
    fun getAllWeatherHistory(): List<HistoryWeatherItem>
}
