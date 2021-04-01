package com.nnsoft.weather.data.dao

import android.util.Log
import com.nnsoft.weather.data.entities.WeatherData
import com.nnsoft.weather.util.Common
import com.vicpin.krealmextensions.queryAsSingle
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Realm
import io.realm.kotlin.where
import java.nio.file.WatchEvent

class WeatherDao {
    fun getWeather(): WeatherData? =
        Realm.getDefaultInstance()
            .where<WeatherData>()
                    .equalTo("id", 1 as Int)
                    .findFirst()

    fun getWeatherFlowable(): Flowable<WeatherData> =
        Realm.getDefaultInstance().where<WeatherData>()
                    .equalTo("id", 1 as Int)
                    .findFirstAsync()
                    .asFlowable()

    fun saveWeather(data: WeatherData) {
        Realm.getDefaultInstance()
            .use { realm ->
                realm.executeTransactionAsync {
                    Log.i("SAVE DATA","data.id="+data.id+" "+data.temp)
                    data.time = Common.timeInMinutes()
                    it.insertOrUpdate(data)
                }
            }
    }
}