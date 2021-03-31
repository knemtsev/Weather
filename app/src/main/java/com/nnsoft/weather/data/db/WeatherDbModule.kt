package com.nnsoft.weather.data.db

import com.nnsoft.weather.data.entities.MyLocation
import com.nnsoft.weather.data.entities.WeatherData
import io.realm.annotations.RealmModule

@RealmModule(classes = [WeatherData::class, MyLocation::class])
class WeatherDbModule {

}
