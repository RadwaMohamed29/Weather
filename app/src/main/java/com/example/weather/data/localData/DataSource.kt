package com.example.weather.data.localData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weather.model.WeatherApi

class DataSource {
    var dao:Dao?

    constructor(app:Application){
        dao= Database.getWeatherDatabase(app)?.dao()
    }
//    fun getAllWeathers():LiveData<List<WeatherApi>>{
//        return dao.getAllWeathers()
//
//    }



}