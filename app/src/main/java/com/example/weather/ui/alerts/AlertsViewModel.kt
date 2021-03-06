package com.example.weather.ui.alerts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.data.localData.LocalDataSource
import com.example.weather.model.Alarm
import com.example.weather.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel (app:Application):AndroidViewModel(app){
    var repo:Repository
    var id=MutableLiveData<Long>()
    init {
        repo= Repository.getInstance(app)

    }
    fun getAllAlarms(): LiveData<List<Alarm>>{
        return repo.getAllAlarms()
    }

    fun insertAlarm(alarm: Alarm){
    viewModelScope.launch(Dispatchers.IO) {
        id.postValue( repo.insertAlarm(alarm))

    }

    }
    fun deleteAlert(id: Int){
        repo.deleteAlarm(id)
    }


}