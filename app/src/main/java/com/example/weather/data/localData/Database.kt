package com.example.weather.data.localData

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather.model.WeatherApi

@androidx.room.Database(entities = arrayOf(WeatherApi::class),version = 1,exportSchema = false)
@TypeConverters(Converter::class)
abstract class Database : RoomDatabase() {
    abstract fun dao():Dao
    companion object{

        private var INSTANCE:Database?=null
        fun getWeatherDatabase(app:Application):Database?{
            return INSTANCE?: synchronized(this){

                val instance=Room.databaseBuilder(app.applicationContext
                    ,Database::class.java
                    ,"weather name")
                    .build()
                INSTANCE=instance
                instance
            }



        }


    }


}