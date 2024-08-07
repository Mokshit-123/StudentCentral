package com.example.notices.data.logInData

import kotlinx.serialization.Serializable

@Serializable
data class LogInRequest(
    val username:String,
    val password:String
)

@Serializable
data class LogInResponse(
    val message:String,
    val token:String?
)