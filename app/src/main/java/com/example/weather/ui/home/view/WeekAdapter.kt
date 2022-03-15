package com.example.weather.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.CustomRowWeekBinding
import com.example.weather.model.Daily
import com.example.weather.model.Hourly
import java.util.*
import kotlin.collections.ArrayList

class WeekAdapter(var weather:ArrayList<Daily>,var context: Context?) :
        RecyclerView.Adapter<WeekAdapter.DayViewHolder>() {
    fun updateDaily(newDaily: List<Daily>){
        weather.clear()
        weather.addAll(newDaily)
        notifyDataSetChanged()
    }

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
        holder.item.txtDay.text = days(calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,Locale.getDefault()),
            context!!
        )
        Glide.with(context!!).load(getIcon(weather[position].weather.get(0).icon))
            .into(holder.item.imgWeather)
        holder.item.txtDegree.text=(weather[position].temp.min.toInt()).toString()+"°"+"/"+(weather[position].temp.max.toInt()).toString()+"°"
        holder.item.txtWeather.text=weather[position].weather.get(0).description

    }
    override fun getItemCount(): Int {
        return weather.size
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

