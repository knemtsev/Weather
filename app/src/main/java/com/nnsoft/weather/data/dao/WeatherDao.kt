package com.nnsoft.weather.data.dao

import android.util.Log
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.util.Common
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Realm
import io.realm.kotlin.where

class WeatherDao(val realm: Realm) {
    fun getWeather(): WeatherData? =
        realm.where<WeatherData>()
            .equalTo("id", 1 as Int)
            .findFirst()

    fun getWeatherFlowable(): Flowable<WeatherData> =
        realm.where<WeatherData>()
            .equalTo("id", 1 as Int)
            .findFirstAsync()
            .asFlowable()

    fun saveWeather(data: WeatherData) {
        realm.executeTransaction {
            Log.i("SAVE DATA", "data.id=" + data.id + " " + data.temp)
            //data.time = Common.timeInMinutes()
            it.insertOrUpdate(data)
        }
    }
}