package com.example.notices.network

import com.example.notices.data.profileData.Profile
import com.example.notices.data.profileData.ProfileRequest
import retrofit2.http.Body
import retrofit2.http.GET

interface ProfileApiService {
    @GET("api/profile")
    suspend fun getProfile(@Body profileRequest: ProfileRequest) : Profile
}