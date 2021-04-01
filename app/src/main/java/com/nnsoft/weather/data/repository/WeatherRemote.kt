package com.nnsoft.weather.data.repository

import android.location.Location
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.entities.WeatherDataMapper
import com.nnsoft.weather.data.openweather.OpenWeatherService
import com.nnsoft.weather.ui.di.AppId
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.internal.wait
import javax.inject.Inject

class WeatherRemote @Inject constructor(private val weatherService: OpenWeatherService,
                                        private val appId: AppId
) {
    fun getWeather(location: Location): Observable<WeatherData>? {
        return weatherService.getWeather(location.latitude, location.longitude, appId.value)
            .map { WeatherDataMapper().mapRemoteToLocal(it)}
    }
}