package com.example.weather.data.remoteData

import com.example.weather.model.WeatherApi
import retrofit2.Response

interface RemoteDataSourceInterface {
    suspend fun getCurrentWeatherData(lat:String
                                      ,lon:String
                                      , exclude:String
                                      , units:String
                                      , lang:String): Response<WeatherApi>
}