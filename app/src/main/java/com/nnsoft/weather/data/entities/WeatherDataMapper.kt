package com.nnsoft.weather.data.entities

import com.nnsoft.weather.data.openweather.entities.WeatherResult
import com.nnsoft.weather.util.Common
import java.util.*

class WeatherDataMapper {
    fun mapRemoteToLocal(result: WeatherResult?): WeatherData? {
        return if(result!=null){
            WeatherData(
                time = Common.timeInMinutes(),
                iconId = result.weather[0].icon,
                temp = result.main.temp-273.15,
                windSpeed = result.wind.speed,
                windDeg = result.wind.deg,
                name = result.name,
                sunrise = Date(result.sys.sunrise*1000L),
                sunset = Date(result.sys.sunset*1000L),
                timezone = result.timezone,
                dt = Date(result.dt*1000L)
            )
        } else
            null
    }


}