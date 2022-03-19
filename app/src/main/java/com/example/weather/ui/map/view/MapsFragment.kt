package com.example.weather.ui.map.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
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
     var lat: String="0.0"
     var lon: String="0.0"
    lateinit var binding: FragmentMapsBinding
    lateinit var viewModel: MapsViewModel
    lateinit var navController: NavController
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


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
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var direction: Boolean = requireArguments().getBoolean("toMap")
        navController = Navigation.findNavController(view)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        sharedPref =
            requireActivity().getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            MapsViewModel::class.java
        )

        binding.saveBtn.setOnClickListener(View.OnClickListener {
            viewModel.setWeatherData(lat, lon)
            editor.putString("lat", lat)
            editor.putString("lon", lon)

            editor.commit()
            if (!direction) {
                navController = Navigation.findNavController(view)
                navController.navigate(R.id.action_mapsFragment_to_homeFragment2)
                Toast.makeText(requireContext(), "false", Toast.LENGTH_SHORT).show()
            } else {

                navController.popBackStack()
            }
        })
        //Log.i("maaap", " " + lat + " " + lon)

    }
}