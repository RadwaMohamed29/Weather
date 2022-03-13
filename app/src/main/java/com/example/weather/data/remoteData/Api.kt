package com.example.weather.data.remoteData

import com.example.weather.model.WeatherApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

val appid="1dad2028a59dd5ffcd40e0b0f95891b9"
interface Api {
    @GET("data/2.5/onecall")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("exclude") exclude:String,
        @Query("units") units:String,
        @Query("lang") lang:String,
        @Query("appid") appId:String= appid
    ):Response<WeatherApi>

}