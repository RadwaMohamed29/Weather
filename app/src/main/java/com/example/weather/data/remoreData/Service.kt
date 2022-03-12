package com.example.weather.data.remoreData

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service {
    private const val url="https://api.openweathermap.org/"
    fun getCurrentWeather():Api{
        return Retrofit.Builder()
            .baseUrl(url).addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

    }


}