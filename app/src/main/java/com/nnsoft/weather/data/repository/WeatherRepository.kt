package com.nnsoft.weather.data.repository

import android.location.Location
import com.nnsoft.weather.data.entities.MyLocation
import com.nnsoft.weather.data.entities.WeatherData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm

class WeatherRepository(val remote: WeatherRemote, val local: WeatherLocal) {

    fun getWeatherRemote(loc: Location): Flowable<WeatherData> {
        return remote.getWeather(loc)
    }

    fun getWeatherLocal(loc: Location): WeatherData? {
        return local.getWeather()
    }

    fun getWeatherObservable(loc: Location): Observable<WeatherData> = Observable.create { emitter ->


    }

    fun getLocationObservable() : Observable<MyLocation> = Observable.create { emitter ->
        do {
            val myLocation = local.getMyLocation()
            if (myLocation != null)
                emitter.onNext(myLocation)

        } while (myLocation==null)
    }

    fun saveWeather(data: WeatherData?) {
        if(data!=null)
            local.saveWeather(data)
    }
}