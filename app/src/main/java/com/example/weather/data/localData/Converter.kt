package com.example.weather.data.localData

import androidx.room.TypeConverter
import com.example.weather.model.Alerts
import com.example.weather.model.Daily
import com.example.weather.model.Hourly
import com.example.weather.model.Weather
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun hourToJson(hour:List<Hourly>?)=Gson().toJson(hour)
    @TypeConverter
    fun jsonToHour(hour: String) = Gson().fromJson(hour, Array<Hourly>::class.java)?.toList()

    @TypeConverter
    fun dayToJson(day:List<Daily>?)=Gson().toJson(day)
    @TypeConverter
    fun jsonToDay(day:String)=Gson().fromJson(day,Array<Daily>::class.java)?.toList()

    @TypeConverter
    fun alertToJson(alert:List<Alerts>?)=Gson().toJson(alert)
    @TypeConverter
    fun jsonToAlert(alert:String?):List<Alerts>?{
        alert?.let {
            return Gson().fromJson(alert,Array<Alerts>::class.java)?.toList()
        }
        return emptyList()

    }

    @TypeConverter
    fun weatherToJson(weather:List<Weather>)=Gson().toJson(weather)
    @TypeConverter
    fun jsonToWeather(weather:String)=Gson().fromJson(weather,Array<Weather>::class.java)?.toList()
}