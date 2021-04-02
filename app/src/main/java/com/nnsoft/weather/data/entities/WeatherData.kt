package com.nnsoft.weather.data.entities

import com.nnsoft.weather.util.Common
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WeatherData (
    var time: Long=0,
    var iconId: String="",
    var temp: Double=0.0,
    var windSpeed: Double=0.0,
    var windDeg: Int=0,
    var name: String="",
    @PrimaryKey
    var id: Int=1
): RealmObject() {
    override fun toString(): String {
        return name+" "+Common.minutes2DateS(time)+" "+temp+" "+windSpeed+" "+windDeg
    }
}