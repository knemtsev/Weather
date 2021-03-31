package com.nnsoft.weather.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.nnsoft.weather.WeatherApplication
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.repository.WeatherRepository
import com.nnsoft.weather.util.timeInMinutes
import java.util.*

class MainViewModel : ViewModel() {

    val rep: WeatherRepository by lazy { WeatherApplication.instance.weatherRepository }

    var data:WeatherData?=null

    fun initData(loc: Location){
        val weather=rep.getWeatherLocal(loc)
        if(weather==null || timeInMinutes()-weather.time!!>10 ){
            refresh(loc)
        } else
            data=weather
    }

    fun refresh(loc: Location){
        Log.i("REFRESH",loc.latitude.toString())
        rep.getWeatherRemote(loc).subscribe( {
            rep.saveWeather(it)
            data=it
        },{
            Log.e("REFRESH",it.message,it)
        })
    }

    override fun onCleared() {
        super.onCleared()
        rep.deleteMyLocation()
    }
}