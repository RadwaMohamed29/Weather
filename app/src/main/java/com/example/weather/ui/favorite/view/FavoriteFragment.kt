package com.example.weather.ui.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.ui.favorite.viewModel.FavViewModel
import com.example.weather.ui.home.view.DayAdapter


class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var navController: NavController
    lateinit var  favAdapter:FavoriteAdapter
    lateinit var viewModel:FavViewModel
    var lat:String="0.0"
    var lon:String="0.0"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(FavViewModel::class.java)
        navController=Navigation.findNavController(view)
        binding.addBtn.setOnClickListener{
            navController.navigate(FavoriteFragmentDirections.actionFavoriteFragmentToMapsFragment())
        }
        binding.rcFavItem.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        binding.rcFavItem.hasFixedSize()
        favAdapter= FavoriteAdapter(arrayListOf(), requireContext(),viewModel)
        binding.rcFavItem.adapter=favAdapter

        viewModel.getAllWeathers().observe(viewLifecycleOwner,{
            favAdapter.updateHours(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavoriteBinding.inflate(inflater,container,false)

        return binding.root
    }


}