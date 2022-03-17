package com.example.weather.ui.details.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherDetailsBinding
import com.example.weather.model.Repository
import com.example.weather.ui.details.viewModel.DetailsViewModel
import com.example.weather.ui.details.viewModel.DetailsViewModelFactory
import com.example.weather.ui.favorite.viewModel.FavViewModel
import com.example.weather.ui.home.view.DayAdapter
import com.example.weather.ui.home.view.WeekAdapter
import com.example.weather.ui.home.viewModel.HomeViewModel
import com.example.weather.ui.home.viewModel.HomeViewModelFactory
import java.util.*
import kotlin.math.log


class WeatherDetailsFragment : Fragment() {
    lateinit var binding: FragmentWeatherDetailsBinding
    lateinit var navController: NavController

    private val viewModel: DetailsViewModel by viewModels<DetailsViewModel> {
        DetailsViewModelFactory(Repository.getInstance(requireActivity().application))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        binding.backBtn.setOnClickListener{
            navController.popBackStack()
        }
        val lat=requireArguments().getDouble("lat")
        val lon=requireArguments().getDouble("lon")
        //Log.i("maaaaaaaaaap", lat+" "+lon)

        viewModel.insertData(lat.toString(), lon.toString(),"minutely", "metric", "en")

        viewModel.weatherApi.observe(viewLifecycleOwner){
            binding.cityName.text=getCityName(lat.toDouble(), lon.toDouble())
            //binding.cityName.text=getCityName(30.5965, 32.2715)
            binding.cityName.text=getCityName(lat.toDouble(), lon.toDouble())
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
            binding.rcItemDay.layoutManager=
                LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
            binding.rcItemDay.hasFixedSize()
            val dayAdapter= DayAdapter(arrayListOf())
            dayAdapter.updateHourly(it.hourly)
            binding.rcItemDay.adapter=dayAdapter

            binding.rcItemWeek.layoutManager=
                LinearLayoutManager(context, RecyclerView.VERTICAL,false)
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
        binding= FragmentWeatherDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }


}