package com.nnsoft.weather.data.db

import io.realm.DynamicRealm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import java.util.*

object WeatherDb {
    val defRealmCfg by lazy {
        RealmConfiguration.Builder()
            .schemaVersion(3L)
            .migration(MyMigration())
            .name("weather.realm")
            .modules(WeatherDbModule())
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
    }
}

class MyMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var oldVersion = oldVersion

        // DynamicRealm exposes an editable schema
        val schema = realm.schema
        if (oldVersion == 0L) {
            schema.get("WeatherData")!!
                .addField("sunrise", Int::class.java)
                .addField("sunset", Int::class.java)
            oldVersion++
        }

        if (oldVersion == 1L) {
            schema.get("WeatherData")!!
                .addField("timezone", Int::class.java)
                .addField("dt", Date::class.java)
                .setRequired("dt", true)
            oldVersion++
        }

        if (oldVersion == 2L) {
            schema.get("WeatherData")!!
                .addField("sunrise2", Date::class.java)
                .setRequired("sunrise2", true)
                .addField("sunset2", Date::class.java)
                .setRequired("sunset2", true)
                .removeField("sunrise")
                .removeField("sunset")
                .renameField("sunrise2","sunrise")
                .renameField("sunset2","sunset")
            oldVersion++
        }
    }
}