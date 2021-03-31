package com.nnsoft.weather.data.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MyLocation(
    var lat: Double,
    var lon: Double,
    @PrimaryKey val id: Int =1
) : RealmObject()
{ }