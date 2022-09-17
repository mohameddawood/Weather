package com.app.robusta.weather.network

import com.app.robusta.weather.ui.domain.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppAPis  {

    @GET("current.json")
    suspend fun getWeather(
        @Query("key") key:String = "26b5b3e1e8f24071bf994600221709",//move it to gradle
        @Query("q") city:String = "Cairo",
        @Query("aqi") aqi:String = "no",
    ): WeatherResponse

    companion object{ operator fun invoke() {} }
}