package com.example.weather.ui.map.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.databinding.FragmentMapsBinding
import com.example.weather.ui.map.viewModel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {
    lateinit var lat:String
    lateinit var lon:String
    lateinit var binding: FragmentMapsBinding
    lateinit var viewModel: MapsViewModel
    lateinit var navController: NavController

    private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(lat?.toDouble()!!, lon?.toDouble()!!)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
            lat = it.latitude.toString()
            lon = it.longitude.toString()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMapsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val sharedPref= requireActivity().application.getSharedPreferences("weather",Context.MODE_PRIVATE)
        lat= sharedPref?.getString("lat","30.5965").toString()
        lon=sharedPref?.getString("lon","32.2715").toString()
        viewModel=ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(
            MapsViewModel::class.java
        )
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            viewModel.setWeatherData(lat,lon)
            navController.popBackStack()

        })
        Log.i("maaap", " "+lat+" "+lon)





    }
}