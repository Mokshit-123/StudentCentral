package com.example.notices.network

import com.example.notices.data.attendanceData.Attendance
import retrofit2.http.GET
import retrofit2.http.Path

interface AttendanceApiService {
    @GET("api/attendance/{enrollmentNumber}")
    suspend fun getAttendance(@Path("enrollmentNumber") enrollmentNumber:String) : List<Attendance>
}