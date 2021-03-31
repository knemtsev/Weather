package com.nnsoft.weather.data.openweather

import com.nnsoft.weather.data.openweather.entities.WeatherResult
import io.reactivex.Flowable

class OpenWeatherService {
    private val retrofit by lazy { ServiceBuilder.buildService(OpenWeatherApi::class.java) }

    fun getWeather(lat: Double, lon: Double, apiId: String): Flowable<WeatherResult> {
        return retrofit.getWeather(lat.toString(),lon.toString(),apiId)
    }
}