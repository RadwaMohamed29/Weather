package com.example.weather.ui.favorite.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weather.model.Repository
import com.example.weather.model.WeatherApi
import java.util.*

class FavViewModel(app:Application):AndroidViewModel(app) {
    var repo:Repository
    init {
        repo= Repository.getInstance(app)
    }

    fun getAllWeathers(): LiveData<List<WeatherApi>>{
        return repo.getAllWeathers()
    }
//    fun getByLatLon(lat:String,lon:String) {
//        return repo.getByLatLon(lat,lon)
//
//    }
    fun deleteWeatherData(timezone:String){
        repo.deleteData(timezone)
    }

}