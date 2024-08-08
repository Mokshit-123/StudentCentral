package com.example.notices.data.profileData

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "Profile")
@Serializable
data class Profile(
    @PrimaryKey val enrollmentNumber : String,
    val name: String,
    val course: String,
    val session: String,
    val profilePhotoLink: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val address: String,
    val emailId: String,
    val fatherName: String?,
    val motherName: String?
)

data class ProfileRequest(
    val enrollmentNumber: String,
    val token : String
)