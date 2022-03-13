package com.example.weather.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.CustomRowWeekBinding
import com.example.weather.model.Daily
import java.util.*
import kotlin.collections.ArrayList

class WeekAdapter(var weather:List<Daily>,val context: Context) :
        RecyclerView.Adapter<WeekAdapter.DayViewHolder>() {
    class DayViewHolder(var item: CustomRowWeekBinding) : RecyclerView.ViewHolder(item.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val viewBinding =
            CustomRowWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(
            viewBinding
        )
    }
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(weather[position].dt.toLong() * 1000)
        holder.item.txtDay.text = days(calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,Locale.getDefault()),context)
        Glide.with(context).load(weather[position].weather.get(0).icon)
            .into(holder.item.imgWeather)
        holder.item.txtDegree.text=(weather[position].temp.min.toInt()).toString()+"°"+"/"+(weather[position].temp.max.toInt()).toString()+"°"
        holder.item.txtWeather.text=weather[position].weather.get(0).description

    }
    override fun getItemCount(): Int {
        return weather.size
    }

    fun days(Day: String, context: Context): String {

        return when (Day.trim()) {
            "Saturday" -> context.getString(R.string.Saturday)
            "Sunday" -> context.getString(R.string.Sunday)
            "Monday" -> context.getString(R.string.Monday)
            "Tuesday" -> context.getString(R.string.Tuesday)
            "Wednesday" -> context.getString(R.string.Wednesday)
            "Friday" -> context.getString(R.string.Friday)
            "Thursday" -> context.getString(R.string.Thursday)
            else -> Day
        }
    }
}

