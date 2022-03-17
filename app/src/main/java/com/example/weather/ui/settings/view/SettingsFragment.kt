package com.example.weather.ui.settings.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.MainActivity
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.ui.settings.viewModel.SettingViewModel
import java.util.*


class SettingsFragment : Fragment() {
   lateinit var binding:FragmentSettingsBinding
   lateinit var navController: NavController
   lateinit var sharedPref:SharedPreferences
   lateinit var editor:SharedPreferences.Editor
   lateinit var viewModel:SettingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref=requireActivity().getSharedPreferences("weather",Context.MODE_PRIVATE)
        editor=sharedPref.edit()
        viewModel=ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                ).get(SettingViewModel::class.java)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var lang=sharedPref.getString("lang","en")
        var unit=sharedPref.getString("units","metric")
        when(lang){
            "en"->binding.enBtn.isChecked=true
            "ar"->binding.arBtn.isChecked=true
        }
        when(unit){
            "metric" -> binding.cM.isChecked=true
            "imperial" -> binding.fMl.isChecked=true
            "standard" -> binding.kM.isChecked=true
        }


        navController=Navigation.findNavController(view)
        binding.arBtn.setOnClickListener{changeLang("ar")}
        binding.enBtn.setOnClickListener { changeLang("en") }
        binding.radioButtonMap.setOnClickListener {  }
        binding.radioButtonGps.setOnClickListener {  }
        binding.cM.setOnClickListener { changeUnit("metric") }
        binding.fMl.setOnClickListener { changeUnit("imperial") }
        binding.kM.setOnClickListener { changeUnit("standard") }

    }
    fun setLocale(languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    private fun changeUnit(unit:String){
        editor.putString("units",unit)
        editor.commit()
        viewModel.updateData()
        restartApp()
    }
    private fun restartApp()
    {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Runtime.getRuntime().exit(0)

    }
    private fun changeLang(lang:String){
        editor.putString("lang",lang)
        editor.commit()
        viewModel.updateData()
        setLocale(lang)
        restartApp()
    }

}