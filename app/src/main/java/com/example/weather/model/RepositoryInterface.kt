package com.example.weather.model

import androidx.lifecycle.LiveData

interface RepositoryInterface {
    fun getAllWeathers(): LiveData<List<WeatherApi>>
    fun getWeatherApi(timezone: String): WeatherApi
    suspend fun insert(weatherApi: WeatherApi?)
    fun deleteWeather(timezone:String)
    fun update(weatherApi: WeatherApi?)
    suspend fun getCurrentWeatherData(lat:String,lon:String, exclude:String, units:String, lang:String): WeatherApi

}