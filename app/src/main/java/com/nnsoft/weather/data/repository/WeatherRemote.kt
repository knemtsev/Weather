package com.nnsoft.weather.data.repository

import android.location.Location
import com.nnsoft.weather.data.entities.MyLocation
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.entities.WeatherDataMapper
import com.nnsoft.weather.data.openweather.OpenWeatherService
import com.nnsoft.weather.ui.di.AppId
import io.reactivex.Flowable
import javax.inject.Inject

class WeatherRemote @Inject constructor(private val weatherService: OpenWeatherService,
                                        private val appId: AppId
) {
    fun getWeather(location: MyLocation): Flowable<WeatherData> {
        return weatherService.getWeather(location.lat, location.lon, appId.value)
            .map {  WeatherDataMapper().mapRemoteToLocal(it) }
    }
}