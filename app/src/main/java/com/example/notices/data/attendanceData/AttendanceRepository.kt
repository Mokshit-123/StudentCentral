package com.example.notices.data.attendanceData

import com.example.notices.network.AttendanceApiService

interface AttendanceRepository{
    suspend fun getAttendance(enrollmentNumber:String):List<Attendance>
}

interface OfflineAttendanceRepository{
    suspend fun getAttendance():List<Attendance>

    suspend fun deleteAll()

    suspend fun insertAll(attendance: List<Attendance>)
}

class DatabaseAttendanceRepository(private val attendanceDao: AttendanceDao):OfflineAttendanceRepository{
    override suspend fun getAttendance(): List<Attendance> {
        return attendanceDao.getAttendance()
    }

    override suspend fun deleteAll() {
        attendanceDao.deleteAll()
    }

    override suspend fun insertAll(attendance: List<Attendance>) {
        attendanceDao.insertAll(attendance)
    }
}

class NetworkAttendanceRepository(private val apiService: AttendanceApiService):AttendanceRepository{
    override suspend fun getAttendance(enrollmentNumber: String): List<Attendance> {
        return apiService.getAttendance(enrollmentNumber=enrollmentNumber)
    }
}