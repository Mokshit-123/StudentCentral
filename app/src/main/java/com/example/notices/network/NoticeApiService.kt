package com.example.notices.network

import com.example.notices.data.noticeData.Notice
import retrofit2.http.GET

interface NoticeApiService{
    @GET("api/notices")
    suspend fun getAllNotices() : List<Notice>
}


