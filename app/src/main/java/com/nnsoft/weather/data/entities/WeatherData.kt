package com.nnsoft.weather.data.entities

import com.nnsoft.weather.util.Common
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class WeatherData (
    var time: Long=0,
    var iconId: String="",
    var temp: Double=0.0,
    var windSpeed: Double=0.0,
    var windDeg: Int=0,
    var name: String="",
    var sunrise: Date = Date(0L),
    var sunset: Date = Date(0L),
    var timezone: Int=0,
    var dt: Date = Date(0L),
    @PrimaryKey
    var id: Int=1
): RealmObject() {
    override fun toString(): String {
        return name+" "+Common.minutes2DateS(time)+" "+temp+" "+windSpeed+" "+windDeg
    }

    /**
     *  индекс направления ветра от 0 до 7
     */
    fun windIndex(): Int =((windDeg+23)%360)/45
}