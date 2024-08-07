package com.example.notices.data.noticeData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoticeDao {
    @Query("Select * from Notices")
    suspend fun getAllNotices():List<Notice>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices:List<Notice>)

    @Query("DELETE FROM Notices")
    suspend fun deleteAll()
}