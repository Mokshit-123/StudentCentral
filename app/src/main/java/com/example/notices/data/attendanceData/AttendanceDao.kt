package com.example.notices.data.attendanceData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM Attendance")
    suspend fun getAttendance():List<Attendance>

    @Query("DELETE FROM Attendance")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attendance:List<Attendance>)

}