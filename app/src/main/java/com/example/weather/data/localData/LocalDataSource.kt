package com.example.weather.data.localData

import androidx.lifecycle.LiveData
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherApi

class LocalDataSource (private val dao: Dao) : InterfaceLocalDataSource {
    override fun getAllWeathers(): LiveData<List<WeatherApi>> {
        return dao.getAllWeathers()
    }

    override fun getAllData(): List<WeatherApi> {
        return dao.getAllData()
    }

    override fun getWeatherApi(timezone: String): WeatherApi {
        return dao.getWeatherApi(timezone)
    }

    override suspend fun insert(weatherApi: WeatherApi?) {
        weatherApi?.let { dao.insert(it) }
    }

    override fun getByLatLon(lat: String, lon: String): LiveData<List<WeatherApi>> {
        return dao.getByLatLon(lat,lon)
    }

    override fun deleteWeather(timezone: String) {
        dao.deleteWeather(timezone)
    }

    override fun update(weatherApi: WeatherApi?) {
        weatherApi?.let { dao.update(it) }
    }

    override fun getAllAlarms(): LiveData<List<Alarm>> {
        return dao.getAllAlarms()
    }

    override suspend fun insertAlarm(alarm: Alarm): Long {
        return dao.insertAlarm(alarm)
    }

    override fun deleteAlarm(id: Int) {
       dao.deleteAlarm(id)
    }


}