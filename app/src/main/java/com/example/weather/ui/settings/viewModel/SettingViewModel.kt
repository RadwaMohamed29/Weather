package com.example.weather.ui.settings.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weather.model.Repository

class SettingViewModel(app:Application):AndroidViewModel(app) {
    var repo:Repository= Repository.getInstance(app)

    fun updateData(){
        repo.updateDataBase()
    }

}