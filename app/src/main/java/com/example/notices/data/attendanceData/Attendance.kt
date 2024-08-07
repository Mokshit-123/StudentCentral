package com.example.notices.data.attendanceData

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "Attendance")
@Serializable
data class Attendance(
    @PrimaryKey val subjectName : String,
    val subjectTeacherName : String,
    val totalClasses : Int,
    val attendedClasses :Int
)
