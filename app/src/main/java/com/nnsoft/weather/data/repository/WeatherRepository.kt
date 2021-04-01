package com.nnsoft.weather.data.repository

import android.location.Location
import android.util.Log
import com.nnsoft.weather.data.dao.WeatherDao
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.util.Common
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class WeatherRepository(
    private val remote: WeatherRemote,
    private val weatherDao: WeatherDao
) {

    fun getWeatherRemote(loc: Location)=remote.getWeather(loc)

    fun getWeatherLocal()= weatherDao.getWeather()

    fun getWeatherFlowable() = weatherDao.getWeatherFlowable()

    fun saveWeather(data: WeatherData?) {
        if (data != null)
            weatherDao.saveWeather(data)
    }

    fun remoteFlow(loc: Location, timeout: Int) : Observable<WeatherData>
    = Observable.interval(0, timeout.toLong(), TimeUnit.MINUTES, Schedulers.io())
        .flatMap {
            getWeatherRemote(loc)
        }
}