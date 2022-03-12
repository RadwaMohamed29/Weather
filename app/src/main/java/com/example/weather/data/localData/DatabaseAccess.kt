package com.example.weather.data.localData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weather.model.WeatherApi

class DatabaseAccess {
    var dao:Dao

    constructor(app:Application){
        dao= DataBase.getWeatherDatabase(app).dao()
    }

    fun getAllWeathers():LiveData<List<WeatherApi>>{
        return dao.getAllWeathers()
    }
    fun getWeatherApi(lat:String,lon:String):WeatherApi{
        return dao.getWeatherApi(lat,lon)
    }
    suspend fun insert(weatherApi: WeatherApi){
        dao.insert(weatherApi)
    }
    suspend fun deleteWeather(lat:String,lon:String){
        dao.deleteWeather(lat,lon)
    }




}