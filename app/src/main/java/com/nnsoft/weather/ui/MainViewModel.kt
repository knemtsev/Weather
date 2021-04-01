package com.nnsoft.weather.ui

import android.location.Location
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.nnsoft.weather.WeatherApplication
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.repository.WeatherRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException

class MainViewModel : ViewModel() {

    val rep: WeatherRepository by lazy { WeatherApplication.instance.weatherRepository }


    var data = ObservableField<WeatherData>()

    var errorMessage = ObservableField<String>()

    fun isErrorMessageVisible() =
        if (errorMessage.get().isNullOrEmpty()) View.GONE else View.VISIBLE


    val compositeDisposable = CompositeDisposable()

    fun refresh(loc: Location) {
        Log.i("REFRESH", loc.latitude.toString())

        errorMessage.set("")
        try {
            compositeDisposable.add(
//                Observable.concat(
//                    rep.getWeatherFlowable().toObservable(),
//                    rep.remoteFlow(loc, 1)
//                )
//                rep.getWeatherFlowable().toObservable()
                rep.remoteFlow(loc, 1)
                    .doOnError { e ->
                        var message = e.message.toString()
                        when (e) {
                            is HttpException -> {
                                Log.e("HTTP ERROR", message)
                            }
                            is SocketTimeoutException -> {
                                Log.e("TIMEOUT ERROR", message)
                            }
                            is IOException -> {
                                Log.e("NETWORK ERROR", message)
                            }
                            is CompositeException -> {
                                Log.e("COMPOSITE EXCEPTION", message)
                                val ce = e as CompositeException
                                message =
                                    ce.exceptions.joinToString("\n") { e -> e.message.toString() }
                            }
                            else -> {
                                Log.e("UNKNOWN ERROR", message)
                            }
                        }
                        errorMessage.set(message)
                    }
                .onErrorResumeNext(rep.getWeatherFlowable().toObservable())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
//                    .onErrorReturn {
//                        val localData = rep.getWeatherLocal()
//                        data.set(localData)
//                        Log.i("ON ERROR RETURN", "temp=" + localData?.temp)
//                        localData
//                    }
                    .subscribe({ weatherData ->
                        Log.i(
                            "SAVE WEATHER",
                            "" + weatherData.name + " " + weatherData.temp
                        )
                        rep.saveWeather(weatherData)
                        data.set(weatherData)
                    }, { e ->
                    }, {

                    })
            )

            //compositeDisposable.add(dis)
        } catch (e: Exception) {
            Log.i("ERROR Refresh", e.message.toString())
            compositeDisposable.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
