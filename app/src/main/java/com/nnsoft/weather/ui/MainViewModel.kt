package com.nnsoft.weather.ui

import android.location.Location
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.nnsoft.weather.WeatherApplication
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    val rep: WeatherRepository by lazy { WeatherApplication.instance.weatherRepository }


     var data = ObservableField<WeatherData>()

    val compositeDisposable = CompositeDisposable()

    fun refresh(loc: Location) {
        Log.i("REFRESH", loc.latitude.toString())

        compositeDisposable.add(
            rep.remoteFlow(loc,1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError {
                    Log.e("MainViewModel.Refresh",it.message,it)
                }
                .subscribe {
                    Log.i("SAVE WEATHER", "" + it.temp)
                    rep.saveWeather(it)
                    data.set(it)
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}