package com.nnsoft.weather.data.entities

import com.nnsoft.weather.data.openweather.entities.WeatherResult
import com.nnsoft.weather.util.locRound
import com.nnsoft.weather.util.timeInMinutes

class WeatherDataMapper {
    fun mapRemoteToLocal(result: WeatherResult?): WeatherData? {
        return if(result!=null){
            WeatherData(
                time = timeInMinutes(),
                lat = result.coord.lat,
                lon = result.coord.lon,
                iconId = result.weather[0].icon,
                temp = result.main.temp-273.15,
                windSpeed = result.wind.speed,
                windDeg = result.wind.deg,
                name = result.name
            )
        } else
            null
    }


}