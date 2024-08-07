package com.example.notices.data.noticeData
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "Notices")
@Serializable
data class Notice(
    val description: String,
    @PrimaryKey val id: String,
    val pdfURL: String,
    val timeStamp: String,
    val title: String,
    val type: String,
)