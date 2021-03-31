package com.nnsoft.weather.data.openweather

import com.nnsoft.weather.data.openweather.entities.WeatherResult
import io.reactivex.rxjava3.core.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("weather")
    fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") app_id: String): Flowable<WeatherResult>
}