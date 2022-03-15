package com.example.weather.ui.map.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weather.model.Repository
import com.example.weather.model.WeatherApi

class MapsViewModel(app:Application):AndroidViewModel(app) {
    var repo:Repository
    init {
        repo= Repository.getInstance(app)
    }
    fun setWeatherData(lat:String,lon:String){
        return repo.setData(lat,lon)
    }
}