package com.example.weather.ui.home.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val viewModel:HomeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(Repository.getInstance(requireActivity().application))
    }

    companion object{
        fun newInstance()=HomeFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gps.findDeviceLocation(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
       // 30.5965, 32.2715

       viewModel.insertData(gps.getLatitude().toString(), gps.getLongitude().toString(),"minutely", "metric", "en")
       // viewModel.insertData("30.5965", "32.2715","minutely", "metric", "en")
        viewModel.weatherApi.observe(viewLifecycleOwner){
           binding.cityName.text=getCityName(gps.getLatitude()!!.toDouble(), gps.getLongitude()!!.toDouble())
            //binding.cityName.text=getCityName(30.5965, 32.2715)
            binding.tempTxt.text=it.current.temp.toString()
            binding.txtCondition.text=it.current.weather[0].description
            binding.txtCloud.text=it.current.clouds.toString()
            binding.txtWeather.text=it.current.wind_speed.toString()
            binding.txtPressure.text=it.current.pressure.toString()
            binding.txtHumidity.text=it.current.humidity.toString()
            binding.txtViolet.text=it.current.uvi.toString()
            binding.txtVisibility.text=it.current.visibility.toString()
            binding.imgShowCondition.setImageResource(getIcon(it.current.weather[0].icon))
            binding.tempTxt.text=it.current.temp.toInt().toString()+"°"
            binding.tempFeel.text=it.current.feels_like.toString()
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
            binding.backBtn.setOnClickListener{
                navController.popBackStack()

            }


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
        val geocoder = Geocoder(requireContext(), Locale("en"))
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        Log.i("location", "getCityText: $lat + $lon + $addresses")
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea // damietta
            val country = addresses[0].countryName
            city = "$state, $country"
        }
        return city
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


}