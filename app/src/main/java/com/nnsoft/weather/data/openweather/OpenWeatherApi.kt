package com.nnsoft.weather.data.openweather

import com.nnsoft.weather.data.openweather.entities.WeatherResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("weather")
    fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") app_id: String): Observable<WeatherResult>
}