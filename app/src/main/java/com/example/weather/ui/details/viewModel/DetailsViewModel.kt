package com.example.weather.ui.details.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.weather.model.Repository
import com.example.weather.model.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val repo:Repository): ViewModel()  {
    val weatherApi= MutableLiveData<WeatherApi>()
    lateinit var api:WeatherApi

    fun insertData(lat:String,lon:String,exclude:String,units:String,lang:String){
        val job=viewModelScope.launch (Dispatchers.IO){
            api=repo.getCurrentWeatherData(lat,lon,exclude,units, lang)
            repo.insert(api)
        }
        viewModelScope.launch (Dispatchers.IO){
            job.join()
            val response=repo.getWeatherApi(api.timezone)
            weatherApi.postValue(response)
        }
    }
}