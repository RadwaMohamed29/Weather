package com.example.weather.data.localData

import androidx.lifecycle.LiveData
import com.example.weather.model.WeatherApi

interface InterfaceLocalDataSource {
    fun getAllWeathers(): LiveData<List<WeatherApi>>
    fun getWeatherApi(timezone:String): WeatherApi
    suspend fun insert(weatherApi: WeatherApi?)
    //suspend fun deleteWeather(timezone: String)
    fun update(weatherApi: WeatherApi?)
}