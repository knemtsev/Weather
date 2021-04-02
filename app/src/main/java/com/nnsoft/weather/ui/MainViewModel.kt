package com.nnsoft.weather.ui

import android.location.Location
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.nnsoft.weather.R
import com.nnsoft.weather.Settings
import com.nnsoft.weather.WeatherApplication
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.data.repository.WeatherRepository
import com.nnsoft.weather.util.Common
import com.nnsoft.weather.util.hhmm
import com.nnsoft.weather.util.short
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class MainViewModel : ViewModel() {

    val app by lazy { WeatherApplication.instance }
    val rep: WeatherRepository by lazy { app.weatherRepository }


    var data = ObservableField<WeatherData>()

    var errorMessage = ObservableField<String>()

    fun isErrorMessageVisible() =
        if (errorMessage.get().isNullOrEmpty()) View.GONE else View.VISIBLE

    fun windName(): String {
        return data.get()?.let {
            app.resources.getStringArray(R.array.wind_direction)[data.get()!!.windIndex()]+" "
        } ?: ""

    }

    private val compositeDisposable = CompositeDisposable()

    fun dataTime() = Common.minutes2DateS(data.get()?.time ?: 0)

    fun time(): String {
        var res=""
        val wd=data.get()
        if(wd!=null){
            res+=wd.dt.short()+"\n"
            res+=wd.sunrise.hhmm()+" - "+wd.sunset.hhmm()
        }
        return res
    }

    init {
        init()
    }

    private fun init() {
        Log.i("INIT", "")
        val disposable=rep.getWeatherFlowable().toObservable()
            .doOnError { e -> errorHandling(e) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ weatherData ->
                Log.i("DATA CHANGED", weatherData.toString())
                data.set(weatherData)
            }, { e ->
            }, {
            })
        compositeDisposable.add(disposable)
    }

    var remoteDis: Disposable?=null

    fun refresh(loc: Location, force: Boolean=false) {
        Log.i("REFRESH", loc.latitude.toString())
        errorMessage.set("")
        remoteDis?.dispose()

        val refreshPeriod=Settings.refreshPeriod.toLong()
        var initDelay=0L

        if(!force){
            // при старте (onResume) расчёт initDelay
            val weatherData=data.get()
            if(weatherData!=null){
                initDelay= (refreshPeriod - (Common.timeInMinutes() - weatherData.time)).coerceAtLeast(0)
            }
        }

        Log.i("INIT DELAY",""+initDelay)
        remoteDis= rep.remoteFlow(loc, refreshPeriod, initDelay)
                .doOnError { errorHandling(it) }
                .onErrorResumeNext(rep.getWeatherFlowable().toObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ weatherData ->
                    Log.i("DATA FROM REMOTE", weatherData.toString())
                    rep.saveWeather(weatherData)
                    if(data.get()==null){
                        init()
                    }
                }, { e ->
                }, {

                })
    }

    private fun errorHandling(e: Throwable) {
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
                val ce = e
                message =
                    ce.exceptions.joinToString("\n") { e -> e.message.toString() }
            }
            else -> {
                Log.e("UNKNOWN ERROR", message)
            }
        }
        errorMessage.set(message)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        remoteDis?.dispose()
    }

}

