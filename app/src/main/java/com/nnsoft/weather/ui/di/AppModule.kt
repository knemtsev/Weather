/*
 * Copyright (c) 2021.
 * Nicholas Nemtsev
 * knemtsev@gmail.com
 */

package com.nnsoft.weather.ui.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.lifecycle.SavedStateHandle
import com.nnsoft.weather.WeatherApplication
import com.nnsoft.weather.data.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module()
class AppModule {

    @Singleton
    @Provides
    fun provideAppContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Singleton
    @Provides
    fun provideSavedState(): SavedStateHandle = SavedStateHandle()

    @Singleton
    @Provides
    fun provideWeatherRepository(app: WeatherApplication): WeatherRepository = app.weatherRepository

    @Singleton
    @Provides
    fun provideApplication(): WeatherApplication = WeatherApplication.instance

}