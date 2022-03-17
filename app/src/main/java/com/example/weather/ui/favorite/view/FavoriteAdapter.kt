package com.example.weather.ui.favorite.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.CustomRowFavBinding
import com.example.weather.model.WeatherApi
import com.example.weather.ui.favorite.viewModel.FavViewModel
import java.util.*
import kotlin.collections.ArrayList

class FavoriteAdapter (var favList:ArrayList<WeatherApi>,context:Context,viewModel:FavViewModel):RecyclerView.Adapter<FavoriteAdapter.FavViewHolder>(){
     var context: Context
     var viewModel:FavViewModel
     var sharedPref: SharedPreferences
     var lang: String
     lateinit var geocoder: Geocoder
     lateinit var navController: NavController
    inner class FavViewHolder(var item :CustomRowFavBinding):RecyclerView.ViewHolder(item.root)
    init {
        this.context=context
        this.viewModel=viewModel
        sharedPref = context.getSharedPreferences("weather", Context.MODE_PRIVATE)
        lang=sharedPref.getString("lang","en").toString()
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
        Log.i("nuuull", " "+favList[position].lat +favList[position].lon)

        holder.item.favCard.setOnClickListener {
            val bundle = bundleOf( "lat" to  favList[position].lat, "lon" to favList[position].lon)
            navController=Navigation.findNavController(it)
            navController.navigate(R.id.action_favoriteFragment_to_weatherDetailsFragment,bundle)

        }
    }
    private fun getCityName(lat: Double, lon: Double): String {
        var city = "Unknown!"
        if(lang.equals("en")){
            geocoder = Geocoder(context, Locale("en"))
        }else{ geocoder = Geocoder(context, Locale("ar")) }
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