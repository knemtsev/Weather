package com.nnsoft.weather.data.repository

import android.location.Location
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.entities.WeatherDataMapper
import com.nnsoft.weather.data.openweather.OpenWeatherService
import com.nnsoft.weather.ui.di.AppId
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class WeatherRemote @Inject constructor(private val weatherService: OpenWeatherService,
                                        private val appId: AppId
) {
    fun getWeather(location: Location): Flowable<WeatherData> {
        return weatherService.getWeather(location.latitude, location.longitude, appId.value).map {
            WeatherDataMapper().mapRemoteToLocal(it)
        }
    }
}