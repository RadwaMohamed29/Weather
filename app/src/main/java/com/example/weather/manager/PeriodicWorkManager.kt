package com.example.weather.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.work.*

import com.example.weather.model.Alarm
import com.example.weather.model.Repository
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PeriodicWorkManager(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    var sharedPref: SharedPreferences =
        context.getSharedPreferences("weather", Context.MODE_PRIVATE)
    lateinit var lang: String
    lateinit var lat: String
    lateinit var lon: String
    lateinit var unit: String

    override suspend fun doWork(): Result {
        lang = sharedPref.getString("lang", "en").toString()
        unit = sharedPref.getString("units", "metric").toString()
        lat = sharedPref?.getString("lat", "0").toString()
        lon = sharedPref?.getString("lon", "0").toString()
        var repo: Repository = Repository.getInstance(context)
        var id = inputData.getInt("id", 1)
        var alarm = repo.getAlarm(id)
        var weather = repo.getCurrentWeatherData(lat, lon, "minutely", unit, lang)

        if (checkTime(alarm)) {
            val delay = getDelay(alarm)
            setOneTimeWorkManager(delay, alarm.id, weather.current.weather[0].description)
        }
        return Result.success()
    }

    private fun setOneTimeWorkManager(delay: Long, id: Int, description: String) {
        val data = Data.Builder()
        data.putString("description", description)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
           AlertOneTimeWorkManager::class.java,
        )
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance().enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )

    }

    private fun getDelay(alarm: Alarm): Long {
        val hour =
            TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alarm.startTime - ((hour + minute) - (2 * 3600L))
    }

    private fun checkTime(alarm: Alarm): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = getDateMillis(date)
        return dayNow >= alarm.startDate && dayNow <= alarm.endDate
    }


    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy")
        val d: Date = f.parse(date)
        return (d.time).div(1000)
    }
}