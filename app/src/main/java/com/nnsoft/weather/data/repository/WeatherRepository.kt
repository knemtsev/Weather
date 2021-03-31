package com.nnsoft.weather.data.repository

import android.location.Location
import com.nnsoft.weather.data.dao.MyLocationDao
import com.nnsoft.weather.data.dao.WeatherDao
import com.nnsoft.weather.data.entities.MyLocation
import com.nnsoft.weather.data.entities.WeatherData
import io.reactivex.Flowable
import io.reactivex.Observable

class WeatherRepository(
    private val remote: WeatherRemote,
    private val weatherDao: WeatherDao,
    private val locationDao: MyLocationDao
) {

    fun getWeatherRemote(loc: Location): Flowable<WeatherData> {
        return remote.getWeather(loc)
    }

    fun getWeatherLocal(): Flowable<WeatherData>? {
        return weatherDao.getWeather()
    }

    fun getLocationObservable() : Observable<MyLocation> = Observable.create { emitter ->
        do {
            val myLocation = weatherDao.getMyLocation()
            if (myLocation != null)
                emitter.onNext(myLocation)

        } while (myLocation==null)
    }

    fun saveWeather(data: WeatherData?) {
        if(data!=null)
            weatherDao.saveWeather(data)
    }

    fun deleteMyLocation() {

    }
}