package com.nnsoft.weather.data.dao

import com.nnsoft.weather.data.entities.MyLocation
import com.vicpin.krealmextensions.queryFirstAsync
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.kotlin.where


class MyLocationDao {
    fun getMyLocation(): Flowable<MyLocation> =
        Realm.getDefaultInstance()
            .use { realm ->
                return realm.where<MyLocation>()
                    .equalTo("id", 1 as Int)
                    .findFirstAsync()
                    .asFlowable()
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

}