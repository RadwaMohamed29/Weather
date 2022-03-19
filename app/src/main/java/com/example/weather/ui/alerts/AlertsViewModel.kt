package com.example.weather.ui.alerts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weather.data.localData.LocalDataSource
import com.example.weather.model.Alarm
import com.example.weather.model.Repository

class AlertsViewModel (app:Application):AndroidViewModel(app){
    var repo:Repository
    init {
        repo= Repository.getInstance(app)

    }
    fun getAllAlarms(): LiveData<List<Alarm>>{
        return repo.getAllAlarms()
    }

    fun insertAlarm(alarm: Alarm): Long {

        return repo.insertAlarm(alarm)

    }
    fun deleteAlert(id: Int){
        repo.deleteAlarm(id)
    }


}