package com.example.notices.data.logInData

import com.example.notices.network.LogInApiService
import retrofit2.http.Body

interface LogInRepository{
    suspend fun login(@Body logInRequest: LogInRequest):LogInResponse
}

class NetworkLogInRepository(private val logInApiService: LogInApiService):LogInRepository{
    override suspend fun login(logInRequest: LogInRequest): LogInResponse {
        return logInApiService.login(logInRequest)
    }
}