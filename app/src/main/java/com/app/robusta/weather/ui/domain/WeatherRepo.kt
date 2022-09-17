package com.app.robusta.weather.ui.domain

import com.app.robusta.weather.network.AppAPis
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    private val appAPis: AppAPis
) {

    fun loadWeather() = flow<WeatherResponse> {
        emit(appAPis.getWeather())
    }
}



