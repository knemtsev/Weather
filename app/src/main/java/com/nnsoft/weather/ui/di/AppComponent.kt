/*
 * Copyright (c) 2021.
 * Nicholas Nemtsev
 * knemtsev@gmail.com
 */

package com.nnsoft.weather.ui.di

import android.app.Application
import com.nnsoft.weather.WeatherApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class
    ]
)

interface AppComponent : AndroidInjector<WeatherApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}

