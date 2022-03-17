package com.example.weather.data.localData

import androidx.lifecycle.LiveData
import com.example.weather.model.WeatherApi

interface InterfaceLocalDataSource {
    fun getAllWeathers(): LiveData<List<WeatherApi>>
    fun getWeatherApi(timezone:String): WeatherApi
    suspend fun insert(weatherApi: WeatherApi?)
    fun getByLatLon(lat:String,lon:String) :LiveData<List<WeatherApi>>
    fun deleteWeather(timezone: String)
    fun update(weatherApi: WeatherApi?)
}