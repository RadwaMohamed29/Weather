package com.example.weather.manager

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.weather.R
import com.example.weather.databinding.AlarmDialogeBinding

class Window constructor(private val context:Context,private val desc:String) {
    private var windowManager: WindowManager? = null
    private var customNotificationDialogView: View? = null
    private var binding:AlarmDialogeBinding?= null
    fun startWindow(){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customNotificationDialogView =
            inflater.inflate(R.layout.alarm_dialoge, null)
        binding = AlarmDialogeBinding.bind(customNotificationDialogView!!)

        binding!!.desc.text=desc
        binding!!.addAlarmBtn.setOnClickListener{
            close()
            stopMyService()
        }
        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        val params = WindowManager.LayoutParams(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
        windowManager!!.addView(customNotificationDialogView, params)
    }
    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(
                customNotificationDialogView
            )
            customNotificationDialogView!!.invalidate()
            (customNotificationDialogView!!.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun stopMyService() {
        context.stopService(Intent(context, MyService::class.java))
    }

}