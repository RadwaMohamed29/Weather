package com.example.weather.ui.splash

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.MainActivity
import com.example.weather.R
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    val activityScope= CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //navController= Navigation.findNavController()
        //(requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val exceptionHandlerException= CoroutineExceptionHandler{_,throwable->throwable.printStackTrace()
            Log.i("radwa", "exption")}
        activityScope.launch{
            delay(6000)
            var intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            //navController.navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment2())

        }

    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }




}