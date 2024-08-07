package com.example.notices.network

import com.example.notices.data.logInData.LogInRequest
import com.example.notices.data.logInData.LogInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LogInApiService {
    @POST("/api/login")
    suspend fun login(@Body loginRequest: LogInRequest):LogInResponse
}