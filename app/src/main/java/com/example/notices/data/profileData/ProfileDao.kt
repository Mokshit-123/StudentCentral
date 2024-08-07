package com.example.notices.data.profileData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfileDao {

    @Query("Select * FROM Profile")
    suspend fun getProfile() : Profile

    @Query("DELETE FROM Profile")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(profile: Profile)
}