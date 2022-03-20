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
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.ui.settings.viewModel.SettingViewModel
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var viewModel: SettingViewModel
    lateinit var lat:String
    lateinit var lon:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SettingViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var lang = sharedPref.getString("lang", "en")
        var unit = sharedPref.getString("units", "metric")
        var location = sharedPref.getString("location", "GPS")

        lat = sharedPref?.getString("lat", "0").toString()
        lon = sharedPref?.getString("lon", "0").toString()
        when (lang) {
            "en" -> binding.enBtn.isChecked = true
            "ar" -> binding.arBtn.isChecked = true
        }
//        when (location) {
//            "GPS" -> binding.radioButtonGps.isChecked = true
//            "Map" -> binding.radioButtonMap.isChecked = true
//        }
        if(lat=="0"||lon=="0"){
            binding.radioButtonGps.isChecked = true
        }else
        {binding.radioButtonMap.isChecked = true}
        when (unit) {
            "metric" -> binding.cM.isChecked = true
            "imperial" -> binding.fMl.isChecked = true
            "standard" -> binding.kM.isChecked = true
        }

        binding.arBtn.setOnClickListener { changeLang("ar") }
        binding.enBtn.setOnClickListener { changeLang("en") }
        binding.radioButtonGps.setOnClickListener {
            binding.radioButtonMap.isChecked = false
            editor.putString("lat", "0")
            editor.putString("lon", "0")
            editor.apply()
            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_homeFragment2)}
        binding.cM.setOnClickListener {
            changeUnit("metric")
             }
        binding.fMl.setOnClickListener {
            changeUnit("imperial")
             }
        binding.kM.setOnClickListener {
            changeUnit("standard")
             }
        binding.radioButtonMap.setOnClickListener {
            binding.radioButtonGps.isChecked = false
            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_mapsFragment)
        }
    }

    fun setLocale(languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = requireActivity().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun changeUnit(unit: String) {
        editor.putString("units", unit)
        editor.commit()
        viewModel.updateData()
        restartApp()
    }

    private fun restartApp() {
      //  val intent = Intent(context, MainActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(requireActivity().intent)
        requireActivity().finish()
        //Runtime.getRuntime().exit(0)

    }

    private fun changeLang(lang: String) {
        editor.putString("lang", lang)
        editor.commit()
        viewModel.updateData()
        setLocale(lang)
        restartApp()
    }

}