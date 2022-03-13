package com.example.weather.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.CustomRowDayBinding
import com.example.weather.model.Hourly
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DayAdapter(var hourly: List<Hourly>,val context:Context):RecyclerView.Adapter<DayAdapter.HourlyViewHolder>(){
    inner class HourlyViewHolder(var item:CustomRowDayBinding):RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val viewBinding=CustomRowDayBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HourlyViewHolder(viewBinding)
    }
    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
      holder.item.txtTime.text=timeFormat(hourly[position].dt.toInt())
      holder.item.txtDgree.text=hourly[position].temp.toInt().toString()+"°"
        Glide.with(context).load(hourly[position].weather.get(0).icon)
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
}