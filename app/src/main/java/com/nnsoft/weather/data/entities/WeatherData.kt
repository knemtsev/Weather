package com.nnsoft.weather.data.entities

import android.location.Location
import com.nnsoft.weather.util.UUIDBase64
import com.nnsoft.weather.util.locRound
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WeatherData (
    var time: Long?=null,
    var iconId: String? = null,
    var temp: Double? = null,
    var windSpeed: Double? = null,
    var windDeg: Int?=null,
    var name: String?=null,
    @PrimaryKey
    val id: Int=1
): RealmObject() {
}