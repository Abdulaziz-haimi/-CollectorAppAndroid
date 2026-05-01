package com.watercollector.app.data.remote.model

data class LoginRequest(
    val userName: String,
    val password: String,
    val deviceCode: String,
    val deviceName: String?,
    val deviceModel: String?,
    val appVersion: String?
)

data class LoginResponse(
    val token: String,
    val expiresAt: String,
    val userId: Int,
    val userName: String,
    val fullName: String?,
    val collectorId: Int,
    val collectorName: String,
    val deviceCode: String
)
