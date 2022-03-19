package com.example.weather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alarms")
class Alarm(
    var date: String,
    var startTime: String,
    var endTime: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
