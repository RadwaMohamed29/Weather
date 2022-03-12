package com.example.weather.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.data.localData.DatabaseAccess
import com.example.weather.data.remoreData.Service
import com.example.weather.model.WeatherApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository {
    var localData: DatabaseAccess
    var remoteData: Service
    lateinit var weatherLiveData: MutableLiveData<WeatherApi>

    constructor(app: Application){
        localData = DatabaseAccess(app)
        remoteData=Service
    }
    public fun getWeatherApi(lat:String,lon:String):WeatherApi{
        return localData.getWeatherApi(lat,lon)
    }

    fun weatherData(context: Context,lat: String,lon: String):LiveData<List<WeatherApi>>{
        CoroutineScope(Dispatchers.IO).launch {
            val response=remoteData.getCurrentWeather().getCurrentWeatherData(lat,lon,"minutely","metric","en")
            try {
                if (response.isSuccessful){
                    response.body()?.let {
                        localData.insert(it)
                        weatherLiveData.postValue(it)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
        return localData.getAllWeathers()
    }




}