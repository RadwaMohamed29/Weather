package com.example.weather.data.localData

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.weather.model.WeatherApi
@Dao
interface Dao {
    @Query("SELECT * FROM Weather")
    fun getAllWeathers():LiveData<List<WeatherApi>>


    @Query("SELECT * FROM Weather")
    fun getAllData(): List<WeatherApi>

//    @Query("DELETE FROM Weather")
//    suspend fun deleteAll(weather: WeatherApi)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather:WeatherApi)

    @Query("SELECT * FROM Weather WHERE timezone=:timezone")
    fun getWeatherApi(timezone:String):WeatherApi

    @Query("SELECT * FROM Weather WHERE lat=:lat AND lon=:lon")
    fun getByLatLon(lat:String,lon:String):WeatherApi

    @Query("DELETE FROM Weather WHERE timezone=:timezone")
     fun deleteWeather(timezone: String)

    @Update
    fun update(weather: WeatherApi)

//    @Delete
//    suspend fun deleteALL()


//    @Query("DELETE FROM Weather")
//    suspend fun deleteAll()




}