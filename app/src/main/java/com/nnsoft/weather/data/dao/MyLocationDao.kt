package com.nnsoft.weather.data.dao

import android.location.Location
import android.util.Log
import com.nnsoft.weather.data.entities.MyLocation
import com.vicpin.krealmextensions.queryFirstAsync
import com.vicpin.krealmextensions.transaction
import io.reactivex.Flowable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.kotlin.where


class MyLocationDao {
    fun getMyLocationObservable(): Observable<MyLocation>
    = Observable.create { emitter ->
        val loc=getMyLocation()
        Log.i("---","loc.isValid=${loc.isValid} loc.isLoaded=${loc.isLoaded}")

    }

    fun getMyLocation(): MyLocation? =
        Realm.getDefaultInstance()
            .use { realm ->
                return realm.where<MyLocation>()
                    .equalTo("id", 1 as Int)
                    .findFirst()
            }


    fun deleteMyLocation() =
        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()
            realm.where<MyLocation>()
                .equalTo("id", 1 as Int)
                .findAllAsync()
                .deleteAllFromRealm()
            realm.commitTransaction()
        }

    fun saveLocation(loc: Location) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransactionAsync {
                val myLocation=MyLocation(
                    lat=loc.latitude,
                    lon=loc.longitude
                )
                it.insertOrUpdate(myLocation)
            }
        }
    }

}