package com.nnsoft.weather.data.repository

import android.location.Location
import android.util.Log
import com.nnsoft.weather.data.dao.MyLocationDao
import com.nnsoft.weather.data.dao.WeatherDao
import com.nnsoft.weather.data.entities.MyLocation
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.util.TAG
import io.reactivex.Flowable
import io.reactivex.Observable

class WeatherRepository(
    private val remote: WeatherRemote,
    private val weatherDao: WeatherDao,
    private val locationDao: MyLocationDao
) {

    fun getWeatherRemote(loc: MyLocation): Flowable<WeatherData> {
        return remote.getWeather(loc)
    }

    fun getWeatherLocal(): Flowable<WeatherData>? {
        return weatherDao.getWeather()
    }

    fun saveWeather(data: WeatherData?) {
        if (data != null)
            weatherDao.saveWeather(data)
    }

    fun getLocation() = locationDao.getMyLocation()
    fun deleteMyLocation() = locationDao.deleteMyLocation()

    fun getWeather(timeout: Int): Observable<WeatherData> = Observable.create { emitter ->
        val weatherLocal=getWeatherLocal()
        weatherLocal?.subscribe {
            Log.i(TAG,""+it.name+" "+it.temp)
        }
        val location=getLocation()
        location.subscribe {

        }

    }

    fun setLocation(loc: Location) {
        locationDao.saveLocation(loc)
    }
}