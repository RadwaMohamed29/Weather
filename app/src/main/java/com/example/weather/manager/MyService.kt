package com.example.weather.manager

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.example.weather.util.MyNotification

class MyService : Service() {
    lateinit var desc:String
    var notification=MyNotification(this)


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        desc= intent?.getStringExtra("description")!!
       val notifi= notification.createNotification(desc,"Weather Notification")
        startForeground(1,notifi)

        if (Settings.canDrawOverlays(this)) {
            var window=Window(this,desc)
            window.startWindow()

        }
        return START_NOT_STICKY
    }
}