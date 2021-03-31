package com.nnsoft.weather.ui

import android.location.Location
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.nnsoft.weather.WeatherApplication
import com.nnsoft.weather.data.entities.MyLocation
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    val rep: WeatherRepository by lazy { WeatherApplication.instance.weatherRepository }

    val _location by lazy {
        rep.getLocation()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .firstElement()
        .subscribe {
             location.set(it)
        }
    }

    val location = ObservableField<MyLocation>()

     var data = ObservableField<WeatherData>()

    val compositeDisposable = CompositeDisposable()

    fun refresh(loc: Location) {
        Log.i("REFRESH", loc.latitude.toString())
        rep.setLocation(loc)

        location.set(MyLocation(lat=loc.latitude, lon=loc.longitude))

        compositeDisposable.add(
            rep.getWeatherRemote(MyLocation(lat = loc.latitude, lon=loc.longitude))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError {
                    Log.e("MainViewModel.Refresh",it.message,it)
                }
                .subscribe {
                    Log.i("SAVE WEATHER", "" + it.temp)
                    rep.saveWeather(it)
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        rep.deleteMyLocation()
        compositeDisposable.clear()
    }
}