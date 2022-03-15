package com.example.weather.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.CustomRowDayBinding
import com.example.weather.model.Hourly
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DayAdapter(var hourly: ArrayList<Hourly>):RecyclerView.Adapter<DayAdapter.HourlyViewHolder>(){
    fun updateHourly(newHourly: List<Hourly>){
        hourly.clear()
        hourly.addAll(newHourly)
        notifyDataSetChanged()
    }
    inner class HourlyViewHolder(var item:CustomRowDayBinding):RecyclerView.ViewHolder(item.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val viewBinding=CustomRowDayBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HourlyViewHolder(viewBinding)
    }
    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
      holder.item.txtTime.text=timeFormat(hourly[position].dt.toInt())
      holder.item.txtDgree.text=hourly[position].temp.toInt().toString()+"°"
        Glide.with(holder.item.imgWeather.context).load(getIcon(hourly[position].weather.get(0).icon))
            .into(holder.item.imgWeather)

    }
    private fun timeFormat(mS:Int):String{
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis((mS*1000).toLong())
        val format=SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }


    override fun getItemCount(): Int {
        return hourly.size
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
}