package com.nnsoft.weather.data.dao

import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.util.Common
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.kotlin.where

class WeatherDao {
    fun getWeather(): WeatherData? =
        Realm.getDefaultInstance()
            .use { realm ->
                return realm.where<WeatherData>()
                    .equalTo("id", 1 as Int)
                    .findFirst()
            }


    fun saveWeather(data: WeatherData) {
        Realm.getDefaultInstance()
            .use { realm ->
                realm.executeTransactionAsync {
                    data.time = Common.timeInMinutes()
                    it.insertOrUpdate(data)
                }
            }
    }
}