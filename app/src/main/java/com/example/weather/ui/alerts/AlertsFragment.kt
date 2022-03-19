package com.example.weather.ui.alerts

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.CustomDialogBinding
import com.example.weather.databinding.FragmentAlertsBinding
import com.example.weather.model.Alarm
import com.example.weather.ui.favorite.view.FavoriteAdapter
import com.example.weather.ui.map.viewModel.MapsViewModel
import com.example.weather.util.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.text.DateFormat.getDateInstance
import java.util.*


class AlertsFragment : Fragment() {
    private lateinit var viewModel: AlertsViewModel
    private lateinit var binding: FragmentAlertsBinding
    private lateinit var alertAdabter: AlertsAdapter
    private lateinit var bindingDialog: CustomDialogBinding
    private lateinit var dialog: Dialog
    private var calStart = Calendar.getInstance()
    private var calEnd = Calendar.getInstance()
    private var isedit: Boolean = false
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAlertsBinding.inflate(inflater,container,false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
           AlertsViewModel::class.java
        )
        binding.addBtn.setOnClickListener{
            var alarm= Alarm("","","")
            showDialog(alarm)
        }
        binding.rcFavItem.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        binding.rcFavItem.hasFixedSize()
        alertAdabter= AlertsAdapter(arrayListOf(),viewModel, requireContext())
        binding.rcFavItem.adapter=alertAdabter
        viewModel.getAllAlarms().observe(viewLifecycleOwner,{
            alertAdabter.updateAlarms(it)
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialog(alarmObj: Alarm) {
        dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layout = WindowManager.LayoutParams()
        // change dialog size
        layout.copyFrom(dialog.getWindow()?.attributes)
        layout.width = WindowManager.LayoutParams.MATCH_PARENT
        layout.height = WindowManager.LayoutParams.WRAP_CONTENT

        bindingDialog = CustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        var current = Calendar.getInstance()

        bindingDialog.firstTimeBtn.setOnClickListener {
            val firstTime = TimePickerDialog(
                context,
                { view, h, m ->
                    calStart.set(Calendar.HOUR_OF_DAY, h)
                    calStart.set(Calendar.MINUTE, m)
                    calStart.set(Calendar.SECOND, 0)
                    // alarmObj.start = "$h : $m"
                    val format = SimpleDateFormat("hh:mm aaa")
                    alarmObj.startTime = format.format(calStart.time)
                    bindingDialog.txtFirstTime.setText(format.format(calStart.time))
                    Log.i("alarm", "ss" + format.format(calStart.time))
                },
                current.get(Calendar.HOUR_OF_DAY),
                current.get(Calendar.MINUTE),
                true
            )
            firstTime.show()
        }
        bindingDialog.secondTimeBtn.setOnClickListener {
            val secondTime = TimePickerDialog(
                context,
                { view, h, m ->
                    calStart.set(Calendar.HOUR_OF_DAY, h)
                    calStart.set(Calendar.MINUTE, m)
                    calStart.set(Calendar.SECOND, 0)
                    // alarmObj.start = "$h : $m"
                    val format = SimpleDateFormat("hh:mm aaa")
                    alarmObj.endTime = format.format(calStart.time)
                    bindingDialog.txtSecondTime.setText(format.format(calStart.time))
                    Log.i("alarm", "ss" + format.format(calStart.time))
                },
                current.get(Calendar.HOUR_OF_DAY),
                current.get(Calendar.MINUTE),
                true
            )
            secondTime.show()
        }

        bindingDialog.calenderBtn.setOnClickListener {
            val date = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    calStart.set(Calendar.YEAR, year)
                    calStart.set(Calendar.MONTH, monthOfYear)
                    calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    calEnd.set(Calendar.YEAR, year)
                    calEnd.set(Calendar.MONTH, monthOfYear)
                    calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val dateFormant = SimpleDateFormat("dd-MM-yyyy")
                    val currentDate = dateFormant.parse(dateFormant.format(current.getTime()))
                    val selectedDate = dateFormant.parse(dateFormant.format(calStart.getTime()))
                    if (currentDate.after(selectedDate) && (!currentDate.equals(selectedDate))) {
                        Toast.makeText(context, "Set valid date", Toast.LENGTH_LONG).show()
                    } else {
                        val myFormat = "dd/MM/yyyy"
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        alarmObj.date = sdf.format(calStart.time)
                        val chosenDate = DateFormat.getDateInstance().format(calStart.getTime())
                        bindingDialog.txtDate.setText(chosenDate)
                        //Log.i("alarm", "dd" + chosenDate)
                    }

                },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
            )

            date.show()
        }
        bindingDialog.addAlarmBtn.setOnClickListener{
            viewModel.insertAlarm(alarmObj)
            dialog.cancel()
            var notification= Notification(requireContext())
            notification.createNotification("title","body")

        }

//        bindingDialog.addAlarmBtn.setOnClickListener {
//            if (validateDialog()) {
//                if (calStart.timeInMillis < calEnd.timeInMillis) {
//                    var id = 0
//                    var jop = CoroutineScope(Dispatchers.IO).launch {
//                        id = viewModel.insertAlarmObj(alarmObj).toInt()
//                    }
//                    jop.invokeOnCompletion {
//                        setAlarm(
//                            context?.applicationContext!!,
//                            id,
//                            calStart,
//                        )
//                    }
//                    dialog.dismiss()
//
//                } else {
//                    Toast.makeText(
//                        context,
//                        "Please Make Sure Your Timing is correct",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//            }
//        }


        dialog.show()
        dialog.getWindow()?.setAttributes(layout)
    }
//    private fun validateDialog(): Boolean {
//
//        if (bindingDialog.fromTime.text.isEmpty())
//            Toast.makeText(context,"time is empty", Toast.LENGTH_SHORT).show()
//        else if (bindingDialog.calenderBtn.text.isEmpty())
//            Toast.makeText(context, "Data is empty", Toast.LENGTH_SHORT).show()
//        else
//            return true
//        return false
//    }
//    private fun setAlarm(
//        context: Context,
//        id: Int,
//        calStart: Calendar,
//    ) {
//        val mIntent = Intent(context, myAlarmReceiver::class.java)
//        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        mIntent.putExtra("endTime", calEnd.timeInMillis)
//        mIntent.putExtra("id", id)
//        val mPendingIntent =
//            PendingIntent.getBroadcast(context, id, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        mAlarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP, calStart.timeInMillis,
//            (2 * 1000).toLong(), mPendingIntent
//        )
//    }


}