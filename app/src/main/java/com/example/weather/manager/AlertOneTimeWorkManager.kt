package com.example.weather.manager

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AlertOneTimeWorkManager(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        var desc=inputData.getString("description")
        startMyService(desc!!)


        return Result.success()
    }
    fun startMyService(decs:String){

        val intent = Intent(applicationContext, MyService::class.java)
        intent.putExtra("description", decs)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(applicationContext, intent)
        } else {
            applicationContext.startService(intent)
        }



        }
}