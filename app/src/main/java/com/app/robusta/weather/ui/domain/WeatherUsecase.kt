package com.app.robusta.weather.ui.domain

import com.app.robusta.weather.ui.domain.WeatherRepo
import com.app.robusta.weather.ui.domain.WeatherResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherUsecase @Inject constructor(
    private val  weatherRepo: WeatherRepo
){

    fun getWeatherData(): Flow<WeatherResponse> {
        return weatherRepo.loadWeather()
    }
}
