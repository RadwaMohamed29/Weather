package com.example.weather.data.localData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.model.WeatherApi
@Dao
interface Dao {
    @Query("SELECT * FROM Weather")
    fun getAllWeathers():LiveData<List<WeatherApi>>

    @Query("DELETE FROM Weather")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather:WeatherApi)

    @Query("SELECT * FROM Weather WHERE lat=:lat AND lon=:lon")
    fun getWeatherApi(lat:String,lon:String):LiveData<WeatherApi>


}