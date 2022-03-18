package com.example.weather.ui.home.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.model.Daily
import com.example.weather.model.Hourly
import com.example.weather.model.Repository
import com.example.weather.ui.home.viewModel.HomeViewModel
import com.example.weather.ui.home.viewModel.HomeViewModelFactory
import com.example.weather.util.LocationByGps
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private  var gps:LocationByGps= LocationByGps()
    lateinit var navController: NavController
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var lang: String
    lateinit var unit: String
    lateinit var location:String
    lateinit var tempUnit: String
    lateinit var windSpeedUnit: String
    lateinit var geocoder: Geocoder
    lateinit var lat:String
    lateinit var lon:String
    private val viewModel:HomeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(Repository.getInstance(requireActivity().application))
    }

    companion object{
        fun newInstance()=HomeFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gps.findDeviceLocation(requireActivity())
        sharedPref = requireActivity().getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        lang=sharedPref.getString("lang","en").toString()
        unit=sharedPref.getString("units","metric").toString()
        location=sharedPref.getString("location","GPS").toString()

        lat = sharedPref?.getString("lat", "0").toString()
        lon = sharedPref?.getString("lon", "0").toString()

        setLocale(lang)
        setUnits(unit)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
       // 30.5965, 32.2715
//        val lat=requireArguments().getString("lat")
//        val lon=requireArguments().getString("lon")
        Log.i("Locaation", "onViewCreated: "+lat+lon)


        if(lat.equals("0")||lon.equals("0")){
            viewModel.insertData(gps.getLatitude().toString(), gps.getLongitude().toString(),"minutely", unit, lang)
        }else{
            viewModel.insertData(lat, lon,"minutely", unit, lang)
        }
        viewModel.weatherApi.observe(viewLifecycleOwner){
            if(lat.equals("0")||lon.equals("0")){
                binding.cityName.text=getCityName(gps.getLatitude()!!.toDouble(), gps.getLongitude()!!.toDouble())
            }else{
                binding.cityName.text=getCityName(lat!!.toDouble(), lon!!.toDouble())
            }
            if (lang.equals("en")){
                binding.tempTxt.text=it.current.temp.toString()
                binding.txtCloud.text=it.current.clouds.toString()
                binding.txtWeather.text=it.current.wind_speed.toString()
                binding.txtPressure.text=it.current.pressure.toString()
                binding.txtHumidity.text=it.current.humidity.toString()
                binding.txtViolet.text=it.current.uvi.toString()
                binding.txtVisibility.text=it.current.visibility.toString()
                binding.tempTxt.text=it.current.temp.toInt().toString()+"°"
                binding.tempFeel.text=it.current.feels_like.toString()

            }else
            {
                binding.tempTxt.text=convertNumbersToArabic(it.current.temp.toInt())
                binding.txtCloud.text=convertNumbersToArabic(it.current.clouds)
                binding.txtWeather.text=convertNumbersToArabic(it.current.wind_speed.toInt())
                binding.txtPressure.text=convertNumbersToArabic(it.current.pressure.toInt())
                binding.txtHumidity.text=convertNumbersToArabic(it.current.humidity.toInt())
                binding.txtViolet.text=convertNumbersToArabic(it.current.uvi.toInt())
                binding.txtVisibility.text=convertNumbersToArabic(it.current.visibility.toInt())
                binding.tempTxt.text=convertNumbersToArabic(it.current.temp.toInt())+"°"
                binding.tempFeel.text=convertNumbersToArabic(it.current.feels_like.toInt())

            }

            binding.txtCondition.text=it.current.weather[0].description
            binding.imgShowCondition.setImageResource(getIcon(it.current.weather[0].icon))
            binding.rcItemDay.layoutManager=LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            binding.rcItemDay.hasFixedSize()
            val dayAdapter= DayAdapter(arrayListOf())
            dayAdapter.updateHourly(it.hourly)
            binding.rcItemDay.adapter=dayAdapter

            binding.rcItemWeek.layoutManager=LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            binding.rcItemWeek.hasFixedSize()
            val weekAdapter= WeekAdapter(arrayListOf(),context)
            weekAdapter.updateDaily(it.daily)
            binding.rcItemWeek.adapter=weekAdapter


        }
    }


    fun getIcon(icon:String):Int{
        when (icon){
            "01n"->{return R.drawable.a01n}
            "02n"->{return R.drawable.a02n}
            "03n"->{return R.drawable.a03n}
            "04n"->{return R.drawable.a04n}
            "09n"->{return R.drawable.a09n}
            "10n"->{return R.drawable.a10n}
            "11n"->{return R.drawable.a11n}
            "13n"->{return R.drawable.a13n}
            "01d"->{return R.drawable.a01d}
            "02d"->{return R.drawable.a02d}
            "03d"->{return R.drawable.a03d}
            "04d"->{return R.drawable.a04d}
            "09d"->{return R.drawable.a09d}
            "10d"->{return R.drawable.a10d}
            "11d"->{return R.drawable.a11d}
            "13d"->{return R.drawable.a13d}
            else ->{return R.drawable.a01n}
        }

    }
    private fun getCityName(lat: Double, lon: Double): String {
        var city = "Unknown!"
        if(lang.equals("en")){
             geocoder = Geocoder(requireContext(), Locale("en"))
        }else{ geocoder = Geocoder(requireContext(), Locale("ar")) }
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        Log.i("location", "getCityText: $lat + $lon + $addresses")
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea // damietta
            val country = addresses[0].countryName
            city = "$state, $country"
        }
        return city
    }
    fun convertNumbersToArabic(value: Int): String? {
        return (value.toString() + "")
            .replace("1", "١").replace("2", "٢")
            .replace("3", "٣").replace("4", "٤")
            .replace("5", "٥").replace("6", "٦")
            .replace("7", "٧").replace("8", "٨")
            .replace("9", "٩").replace("0", "٠")
    }

    fun setUnits(unit: String) {
        when (unit) {
            "metric" -> {
                tempUnit = "°c"
                windSpeedUnit = "m/s"
            }
            "imperial" -> {
                tempUnit = "°f"
                windSpeedUnit = "m/h"
            }
            "standard" -> {
                tempUnit = "°k"
                windSpeedUnit = "m/s"
            }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


}