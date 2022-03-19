package com.example.weather.data.localData

import androidx.lifecycle.LiveData
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherApi

interface InterfaceLocalDataSource {
    fun getAllWeathers(): LiveData<List<WeatherApi>>
    fun getAllData():List<WeatherApi>
    fun getWeatherApi(timezone:String): WeatherApi
    suspend fun insert(weatherApi: WeatherApi?)
    fun getByLatLon(lat:String,lon:String) :LiveData<List<WeatherApi>>
    fun deleteWeather(timezone: String)
    fun update(weatherApi: WeatherApi?)
    fun getAllAlarms(): LiveData<List<Alarm>>
    suspend fun insertAlarm(alarmObj: Alarm): Long
    fun deleteAlarm(id: Int): Unit
}