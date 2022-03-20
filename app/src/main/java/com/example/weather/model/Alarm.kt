package com.example.weather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alarms")
class Alarm(
    var startDate: Long,
    var endDate:Long,
    var startTime: Long,
    var endTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
