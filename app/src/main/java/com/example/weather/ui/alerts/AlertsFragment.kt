package com.example.weather.ui.alerts

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.weather.databinding.CustomDialogBinding
import com.example.weather.databinding.FragmentAlertsBinding
import com.example.weather.manager.PeriodicWorkManager
import com.example.weather.model.Alarm
import com.example.weather.util.MyNotification
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AlertsFragment : Fragment() {
    private lateinit var viewModel: AlertsViewModel
    private lateinit var binding: FragmentAlertsBinding
    private lateinit var alertAdabter: AlertsAdapter
    private lateinit var bindingDialog: CustomDialogBinding
    private lateinit var dialog: Dialog
    private var calStart = Calendar.getInstance()
    private var calEnd = Calendar.getInstance()
    lateinit var navController: NavController
    lateinit var alarm: Alarm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            AlertsViewModel::class.java
        )
        binding.addBtn.setOnClickListener {
            checkDrawOverlayPermission()
            alarm = Alarm(0, 0, 0, 0)
            showDialog(alarm)


        }
        binding.rcFavItem.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rcFavItem.hasFixedSize()
        alertAdabter = AlertsAdapter(arrayListOf(), viewModel, requireContext())
        binding.rcFavItem.adapter = alertAdabter
        viewModel.getAllAlarms().observe(viewLifecycleOwner) {
            alertAdabter.updateAlarms(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            // if not construct intent to request permission
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("Permissions")
                .setMessage("Permissions needed to add you alarm :)")
                .setPositiveButton("yes") { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    startActivityForResult(
                        intent,
                        1
                    )
                    dialog.dismiss()
                    alarm = Alarm(0, 0, 0, 0)
                    showDialog(alarm)
                }.setNegativeButton(
                    "No"
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    alarm = Alarm(0, 0, 0, 0)
                    showDialog(alarm)
                }.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialog(alarmObj: Alarm) {
        dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layout = WindowManager.LayoutParams()
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

                    var time = TimeUnit.MINUTES.toSeconds(m.toLong()) + TimeUnit.HOURS.toSeconds(
                        h.toLong()
                    )

                    time = time.minus(3600L * 2)
                    alarmObj.startTime = time

                    bindingDialog.txtFirstTime.text = convertToTime(time, requireContext())
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

                    var time = TimeUnit.MINUTES.toSeconds(m.toLong()) + TimeUnit.HOURS.toSeconds(
                        h.toLong()
                    )

                    time = time.minus(3600L * 2)
                    alarmObj.endTime = time
                    bindingDialog.txtSecondTime.text = convertToTime(time, requireContext())
                },
                current.get(Calendar.HOUR_OF_DAY),
                current.get(Calendar.MINUTE),
                true
            )
            secondTime.show()
        }

        bindingDialog.startDateBtn.setOnClickListener {
            val date = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    calStart.set(Calendar.YEAR, year)
                    calStart.set(Calendar.MONTH, monthOfYear)
                    calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    calEnd.set(Calendar.YEAR, year)
                    calEnd.set(Calendar.MONTH, monthOfYear)
                    calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = "$dayOfMonth/${monthOfYear + 1}/$year"
                    var dateLong = getDateMillis(date)
                    bindingDialog.startDate.text = convertToDate(dateLong, requireContext())

                    alarmObj.startDate = dateLong

                },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
            )

            date.show()
        }


        bindingDialog.endDateBtn.setOnClickListener {
            val date = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    calStart.set(Calendar.YEAR, year)
                    calStart.set(Calendar.MONTH, monthOfYear)
                    calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    calEnd.set(Calendar.YEAR, year)
                    calEnd.set(Calendar.MONTH, monthOfYear)
                    calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = "$dayOfMonth/${monthOfYear + 1}/$year"
                    var dateLong = getDateMillis(date)
                    bindingDialog.endDate.text = convertToDate(dateLong, requireContext())

                    alarmObj.endDate = dateLong
                },
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
            )

            date.show()
        }
        bindingDialog.addAlarmBtn.setOnClickListener {
            viewModel.insertAlarm(alarmObj)
            dialog.cancel()
        }
        viewModel.id.observe(viewLifecycleOwner) {
            if (it != null) {
                setPeriodicWorkManager(it.toInt())

            }
        }

        bindingDialog.cancelBtn.setOnClickListener{
            dialog.cancel()
        }

        dialog.show()
        dialog.getWindow()?.setAttributes(layout)
    }

    private fun setPeriodicWorkManager(id: Int) {
        val data = Data.Builder()
        data.putInt("id", id)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            PeriodicWorkManager::class.java,
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )

    }

    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy")
        val d: Date = f.parse(date)
        return (d.time).div(1000)
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


}