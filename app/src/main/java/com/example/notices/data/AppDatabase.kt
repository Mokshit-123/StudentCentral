package com.example.notices.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notices.data.attendanceData.Attendance
import com.example.notices.data.attendanceData.AttendanceDao
import com.example.notices.data.noticeData.Notice
import com.example.notices.data.noticeData.NoticeDao
import com.example.notices.data.profileData.Profile
import com.example.notices.data.profileData.ProfileDao

@Database(entities = [Notice ::class, Attendance::class, Profile ::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase(){
    abstract fun noticeDao() : NoticeDao

    abstract fun attendanceDao():AttendanceDao

    abstract fun profileDao():ProfileDao

    companion object{
        @Volatile
        private var Instance : AppDatabase? =  null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}