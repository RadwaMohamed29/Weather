package com.example.weather.data.localData

import androidx.room.TypeConverter
import com.example.weather.model.Alerts
import com.example.weather.model.Daily
import com.example.weather.model.Hourly
import com.example.weather.model.Weather
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun listHourlyTojson(value: List<Hourly>) = Gson().toJson(value)

    @TypeConverter
    fun listDailyToJson(value: List<Daily>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToHourlyList(value: String) = Gson().fromJson(value, Array<Hourly>::class.java).toList()

    @TypeConverter
    fun jsonToDailyList(value: String) = Gson().fromJson(value, Array<Daily>::class.java).toList()

    @TypeConverter
    fun listWeatherToJson(value: List<Weather>) = Gson().toJson(value)

    @TypeConverter
    fun jsonTolistWeather(value: String) = Gson().fromJson(value, Array<Weather>::class.java).toList()

    @TypeConverter
    fun  listAlertToJson (value:List<Alerts>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToAlertList(value: String?): List<Alerts>? {
        value?.let {
            return Gson().fromJson(value, Array<Alerts>::class.java)?.toList()
        }
        return emptyList()
    }
}