package com.nnsoft.weather.data.db

import io.realm.RealmConfiguration

object WeatherDb {
    val defRealmCfg by lazy { RealmConfiguration.Builder()
                    .schemaVersion(0)
                    .name("weather.realm")
                    .modules(WeatherDbModule())
                    .allowWritesOnUiThread(true)
                    .allowQueriesOnUiThread(true)
                    .build() }
}
