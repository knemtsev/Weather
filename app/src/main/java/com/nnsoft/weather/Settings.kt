package com.nnsoft.weather

import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object Settings {
    const val REFRESH_PERIOD="refresh_period"
    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            WeatherApplication.instance.applicationContext
        )
    }

    val refreshPeriod: Int
        get() = prefs.getString(REFRESH_PERIOD, "10")?.toInt() ?: 10
}