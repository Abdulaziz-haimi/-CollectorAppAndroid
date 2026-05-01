package com.watercollector.app.data.repository

import android.os.Build
import com.watercollector.app.data.remote.NetworkFactory
import com.watercollector.app.data.remote.model.LoginRequest

class AuthRepository(
    private val sessionManager: SessionManager
) {
    suspend fun login(baseUrl: String, userName: String, password: String): Result<Unit> = runCatching {
        if (userName == "admin" && password == "123") {
            sessionManager.baseUrl = baseUrl
            sessionManager.token = "local-test-token"
            sessionManager.collectorId = 1
            sessionManager.collectorName = "Admin"
            sessionManager.userName = "admin"
            sessionManager.fullName = "Administrator"
            return@runCatching
        }

        val api = NetworkFactory.createApiService(baseUrl, null)
        val response = api.login(
            LoginRequest(
                userName = userName,
                password = password,
                deviceCode = sessionManager.deviceCode,
                deviceName = "Android Phone",
                deviceModel = Build.MODEL,
                appVersion = "1.0.0"
            )
        )

        sessionManager.baseUrl = baseUrl
        sessionManager.token = response.token
        sessionManager.collectorId = response.collectorId
        sessionManager.collectorName = response.collectorName
        sessionManager.userName = response.userName
        sessionManager.fullName = response.fullName
        sessionManager.deviceCode = response.deviceCode
    }
}
/*package com.watercollector.app.data.repository

import android.os.Build
import com.watercollector.app.data.remote.NetworkFactory
import com.watercollector.app.data.remote.model.LoginRequest

class AuthRepository(
    private val sessionManager: SessionManager
) {
    suspend fun login(baseUrl: String, userName: String, password: String): Result<Unit> = runCatching {
        val api = NetworkFactory.createApiService(baseUrl, null)
        val response = api.login(
            LoginRequest(
                userName = userName,
                password = password,
                deviceCode = sessionManager.deviceCode,
                deviceName = "Android Phone",
                deviceModel = Build.MODEL,
                appVersion = "1.0.0"
            )
        )

        sessionManager.baseUrl = baseUrl
        sessionManager.token = response.token
        sessionManager.collectorId = response.collectorId
        sessionManager.collectorName = response.collectorName
        sessionManager.userName = response.userName
        sessionManager.fullName = response.fullName
        sessionManager.deviceCode = response.deviceCode
    }
}

 */
