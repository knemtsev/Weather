/*
 * Copyright (c) 2021.
 * Nicholas Nemtsev
 * knemtsev@gmail.com
 */

package com.nnsoft.weather.ui.di

import com.nnsoft.weather.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}