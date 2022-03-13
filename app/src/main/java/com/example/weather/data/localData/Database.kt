package com.example.weather.data.localData

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather.model.WeatherApi

@Database(entities = arrayOf(WeatherApi::class), version = 1, exportSchema = false)
@TypeConverters(Converter::class)
 abstract class DataBase : RoomDatabase() {
    abstract fun dao(): Dao
    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getWeatherDatabase(application: Application): DataBase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    DataBase::class.java,
                    "weather"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}


