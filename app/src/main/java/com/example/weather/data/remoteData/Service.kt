package com.example.weather.data.remoteData

import com.example.weather.model.WeatherApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service :RemoteDataSourceInterface{
    private const val url="https://api.openweathermap.org/"
   private fun getCurrentWeather():Api{
        return Retrofit.Builder()
            .baseUrl(url).addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

    }
    override suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        exclude: String,
        units: String,
        lang: String
    ): Response<WeatherApi> = getCurrentWeather().getCurrentWeatherData(lat, lon, exclude, units, lang)


}