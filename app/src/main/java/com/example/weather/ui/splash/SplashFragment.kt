package com.example.weather.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.databinding.FragmentSplashBinding
import com.example.weather.ui.home.HomeFragment
import kotlinx.coroutines.*

class SplashFragment : Fragment() {
    val activityScope= CoroutineScope(Dispatchers.Main)
    private lateinit var navController: NavController
    private  var _binding: FragmentSplashBinding?=null
    private val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        //(requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val exceptionHandlerException= CoroutineExceptionHandler{_,throwable->throwable.printStackTrace()
            Log.i("radwa", "exption")}
        activityScope.launch{
            delay(6000)
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment2())

        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }

  

}