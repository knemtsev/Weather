package com.nnsoft.weather.data.openweather

import com.nnsoft.weather.data.openweather.entities.WeatherResult
import io.reactivex.rxjava3.core.Flowable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherService {
    private val retrofit by lazy { ServiceBuilder.buildService(OpenWeatherApi::class.java) }

    fun getWeather(lat: Double, lon: Double, apiId: String): Flowable<WeatherResult> {
        return retrofit.getWeather(lat.toString(),lon.toString(),apiId)
    }
}