package com.nnsoft.weather.data.repository

import android.location.Location
import com.nnsoft.weather.data.entities.MyLocation
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.util.Common
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*

class WeatherLocal {

    fun getWeather(): WeatherData? =
        Realm.getDefaultInstance()
            .use { realm ->
                return realm.where<WeatherData>()
                    .equalTo("id", 1 as Int)
                    .findFirstAsync()
            }


    fun saveWeather(data: WeatherData) {
        Realm.getDefaultInstance()
            .use { realm ->
                realm.executeTransaction {
                    data.time = Common.timeInMinutes()
                    it.insertOrUpdate(data)
                }
            }
    }

    fun getMyLocation(): MyLocation? =
        Realm.getDefaultInstance()
        .use { realm ->
            return realm.where<MyLocation>()
                .equalTo("id", 1 as Int)
                .findFirstAsync()
        }

}