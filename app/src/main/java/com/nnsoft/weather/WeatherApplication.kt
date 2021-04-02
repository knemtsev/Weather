package com.nnsoft.weather

import com.nnsoft.weather.data.dao.WeatherDao
import com.nnsoft.weather.data.db.WeatherDb
import com.nnsoft.weather.data.openweather.OpenWeatherService
import com.nnsoft.weather.data.repository.WeatherRemote
import com.nnsoft.weather.data.repository.WeatherRepository
import com.nnsoft.weather.ui.di.AppId
import com.nnsoft.weather.ui.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.realm.Realm

class WeatherApplication: DaggerApplication() {
    companion object {
        lateinit var instance: WeatherApplication
    }

    private val appid by lazy { AppId(BuildConfig.APPID) }

    val realm: Realm by lazy { Realm.getDefaultInstance() }
    val weatherRepository by lazy{ WeatherRepository(WeatherRemote(OpenWeatherService(),appid),
        weatherDao = WeatherDao(realm)
        )}


    override fun onCreate() {
        super.onCreate()
        instance=this
        Realm.init(this)
        Realm.setDefaultConfiguration(WeatherDb.defRealmCfg)
    }

    override fun onTerminate() {
        super.onTerminate()
        realm.close()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}