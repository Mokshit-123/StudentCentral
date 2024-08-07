package com.example.notices.network

import com.example.notices.data.profileData.Profile
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApiService {
    @GET("api/profile/{enrollmentNumber}")
    suspend fun getProfile(@Path("enrollmentNumber") enrollmentNumber: String) : Profile
}