package com.example.weather.ui.favorite.view

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.CustomRowFavBinding
import com.example.weather.model.WeatherApi
import com.example.weather.ui.favorite.viewModel.FavViewModel
import java.util.*
import kotlin.collections.ArrayList

class FavoriteAdapter (var favList:ArrayList<WeatherApi>,context:Context,viewModel:FavViewModel):RecyclerView.Adapter<FavoriteAdapter.FavViewHolder>(){
     var context: Context
     var viewModel:FavViewModel
     lateinit var navController: NavController
    inner class FavViewHolder(var item :CustomRowFavBinding):RecyclerView.ViewHolder(item.root)
    init {
        this.context=context
        this.viewModel=viewModel

    }

    fun updateHours(newList:List<WeatherApi>){
        favList.clear()
        favList.addAll(newList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding=CustomRowFavBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.item.txtCountry.text=getCityName(favList[position].lat,favList[position].lon)
        holder.item.deleteIcon.setOnClickListener{
            viewModel.deleteWeatherData(favList[position].timezone)
        }
        holder.item.favCard.setOnClickListener {
            navController=Navigation.findNavController(it)
            navController.navigate(FavoriteFragmentDirections.actionFavoriteFragmentToHomeFragment22())

        }
    }
    private fun getCityName(lat: Double, lon: Double): String {
        var city = "Unknown!"
        val geocoder = Geocoder(context, Locale("en"))
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        Log.i("location", "getCityText: $lat + $lon + $addresses")
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea // damietta
            val country = addresses[0].countryName
            city = "$state, $country"
        }
        return city
    }

    override fun getItemCount(): Int {
         return favList.size
    }
}