package com.nnsoft.weather.data.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MyLocation(
    var lat: Double=0.0,
    var lon: Double=0.0,
    @PrimaryKey var id: Int =1
) : RealmObject()
{ }