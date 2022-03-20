package com.example.weather.ui.alerts

import android.app.*
import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager

import com.example.weather.databinding.CustomRowAlarmBinding
import com.example.weather.model.Alarm
import java.text.SimpleDateFormat
import java.util.*

class AlertsAdapter(
    var alarmList: ArrayList<Alarm>,
    alartViewModel: AlertsViewModel,
    context: Context
) : RecyclerView.Adapter<AlertsAdapter.VH>() {
    var context: Context
    var alartViewModel: AlertsViewModel
    var notificationUtils: Notification

    init {
        this.context = context
        this.alartViewModel = alartViewModel
        notificationUtils = Notification()
    }


    fun updateAlarms(newAlarmList: List<Alarm>) {
        alarmList.clear()
        alarmList.addAll(newAlarmList)
        notifyDataSetChanged()
    }

    class VH(var myView: CustomRowAlarmBinding) : RecyclerView.ViewHolder(myView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewBinding =
            CustomRowAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(viewBinding)
    }

    fun convertToTime(dt: Long, context: Context): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("h:mm a")
        return format.format(date)
    }

    fun convertToDate(dt: Long, context: Context): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("d MMM, yyyy")
        return format.format(date)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.myView.title.text = convertToTime(
            alarmList[position].startTime,
            context
        ).plus("-").plus(convertToTime(alarmList[position].endTime, context))

        holder.myView.desc.text = convertToDate(
            alarmList[position].startDate,
            context
        ).plus("-").plus(convertToDate(alarmList[position].endDate, context))

        holder.myView.deleteBtn.setOnClickListener {
            alartViewModel.deleteAlert(alarmList[position].id)
            WorkManager.getInstance().cancelAllWorkByTag("${alarmList[position].id}")

        }
    }
    override fun getItemCount() = alarmList.size

}







