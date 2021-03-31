package com.nnsoft.weather.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.nnsoft.weather.WeatherApplication
import com.nnsoft.weather.data.entities.MyLocation
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    val rep: WeatherRepository by lazy { WeatherApplication.instance.weatherRepository }

    val _location by lazy { rep.getLocation() }

    var location: MyLocation?=null
        get() {
            var res:MyLocation?=null
            _location.subscribe {
                res=it
            }.dispose()
            return res
        }

    val _data by lazy { rep.getWeather(10) }

    var data: WeatherData? = null

    fun refresh(loc: Location){
        Log.i("REFRESH",loc.latitude.toString())
        rep.setLocation(loc)
        rep.getWeatherRemote(MyLocation())
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i("SAVE WEATHER",""+it.temp)
                rep.saveWeather(it)
            }.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        rep.deleteMyLocation()
    }
}