package com.example.weather.data.localData

import androidx.lifecycle.LiveData
import com.example.weather.model.WeatherApi

class LocalDataSource (private val dao: Dao) : InterfaceLocalDataSource {
    override fun getAllWeathers(): LiveData<List<WeatherApi>> {
        return dao.getAllWeathers()
    }

    override fun getWeatherApi(timezone: String): WeatherApi {
        return dao.getWeatherApi(timezone)
    }

    override suspend fun insert(weatherApi: WeatherApi?) {
        weatherApi?.let { dao.insert(it) }
    }

//    override suspend fun deleteWeather(timezone: String) {
//        dao.deleteWeather(timezone)
//    }

    override fun update(weatherApi: WeatherApi?) {
        weatherApi?.let { dao.update(it) }
    }


}