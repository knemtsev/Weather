package com.nnsoft.weather

import android.app.Application
import com.nnsoft.weather.data.dao.MyLocationDao
import com.nnsoft.weather.data.dao.WeatherDao
import com.nnsoft.weather.data.db.WeatherDb
import com.nnsoft.weather.data.openweather.OpenWeatherService
import com.nnsoft.weather.data.repository.WeatherLocal
import com.nnsoft.weather.data.repository.WeatherRemote
import com.nnsoft.weather.data.repository.WeatherRepository
import com.nnsoft.weather.ui.di.AppId
import com.nnsoft.weather.ui.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.realm.Realm
import javax.inject.Inject

class WeatherApplication: DaggerApplication() {
    companion object {
        lateinit var instance: WeatherApplication
    }

    private val appid by lazy { AppId(BuildConfig.APPID) }

    val weatherRepository = WeatherRepository(WeatherRemote(OpenWeatherService(),appid),
        weatherDao = WeatherDao(),
        locationDao = MyLocationDao()
        )


    override fun onCreate() {
        super.onCreate()
        instance=this
        Realm.init(this)
        Realm.setDefaultConfiguration(WeatherDb.defRealmCfg)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}