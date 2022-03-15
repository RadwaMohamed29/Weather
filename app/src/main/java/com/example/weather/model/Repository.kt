package com.example.weather.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.data.localData.DataBase
import com.example.weather.data.localData.LocalDataSource
import com.example.weather.data.localData.InterfaceLocalDataSource
import com.example.weather.data.remoteData.RemoteDataSourceInterface
import com.example.weather.data.remoteData.Service
import kotlinx.coroutines.*
import java.lang.Exception

class Repository private constructor(private var localData: InterfaceLocalDataSource, private var remoteData:RemoteDataSourceInterface):RepositoryInterface {
    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(app: Application): Repository {
            return INSTANCE ?: synchronized(this) {
               Repository(LocalDataSource(DataBase.getWeatherDatabase(app).dao()), Service)
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }

    lateinit var weatherLiveData: MutableLiveData<WeatherApi>
    override fun getAllWeathers(): LiveData<List<WeatherApi>> {
        return localData.getAllWeathers()
    }

    override fun getWeatherApi(timezone:String): WeatherApi {
        return localData.getWeatherApi(timezone)
    }

    override suspend fun insert(weatherApi: WeatherApi?) {
        return localData.insert(weatherApi)
    }

    override fun deleteWeather(timezone: String) {
        return localData.deleteWeather(timezone)
    }

//    override suspend fun deleteWeather(timezone: String) {
//        return localData.deleteWeather(timezone)
//    }

    override fun update(weatherApi: WeatherApi?) {
        return localData.update(weatherApi)
    }
    fun setData(lat: String, log: String) {

        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.i("id", "exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            val response = Service
                .getCurrentWeatherData(lat, log, "minutely", "units", "lang")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    localData.insert(response.body())
                    //  weatherLiveData.postValue(response.body())
                }
            }
        }
    }
//    fun getByLatLon(lat:String,lon:String){
//        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
//            throwable.printStackTrace()
//            Log.i("id", "exception")
//        }
//        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
//            localData.getByLatLon(lat,lon)
//
//        }
//    }


    fun deleteData(timezone: String?) {
        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.i("id", "exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            timezone?.let { localData.deleteWeather(it) }

        }
    }


    override suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        exclude: String,
        units: String,
        lang: String
    ): WeatherApi{
        val response=remoteData.getCurrentWeatherData(lat,lon,"minutely",units,lang)
        if (response.isSuccessful){return response.body()!!}
        else{throw Exception("${response.errorBody()}")}
    }


}








