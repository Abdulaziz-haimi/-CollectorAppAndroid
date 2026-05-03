package com.watercollector.app.data.repository

import android.os.Build
import com.watercollector.app.data.remote.NetworkFactory
import com.watercollector.app.data.remote.model.LoginRequest
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository(
    private val sessionManager: SessionManager
) {
    suspend fun login(
        baseUrl: String,
        userName: String,
        password: String
    ): Result<Unit> {
        val normalizedBaseUrl = SessionManager.normalizeBaseUrl(baseUrl)

        return try {
            val api = NetworkFactory.createApiService(
                baseUrl = normalizedBaseUrl,
                token = null
            )

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

            sessionManager.baseUrl = normalizedBaseUrl
            sessionManager.token = response.token
            sessionManager.collectorId = response.collectorId
            sessionManager.collectorName = response.collectorName
            sessionManager.userName = response.userName
            sessionManager.fullName = response.fullName
            sessionManager.deviceCode = response.deviceCode

            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception(readApiError(e)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun readApiError(e: HttpException): String {
        val fallback = "فشل تسجيل الدخول: HTTP ${e.code()}"

        return try {
            val body = e.response()?.errorBody()?.string()

            if (body.isNullOrBlank()) {
                fallback
            } else {
                val json = JSONObject(body)
                json.optString("message", fallback)
            }
        } catch (_: Exception) {
            fallback
        }
    }
}
/*package com.watercollector.app.data.repository

import android.os.Build
import com.watercollector.app.data.remote.NetworkFactory
import com.watercollector.app.data.remote.model.LoginRequest

class AuthRepository(
    private val sessionManager: SessionManager
) {
    suspend fun login(
        baseUrl: String,
        userName: String,
        password: String
    ): Result<Unit> = runCatching {

        val normalizedBaseUrl = normalizeBaseUrl(baseUrl)

        require(normalizedBaseUrl.startsWith("http://") || normalizedBaseUrl.startsWith("https://")) {
            "رابط الخادم يجب أن يبدأ بـ http:// أو https://"
        }

        require(userName.isNotBlank()) {
            "اسم المستخدم مطلوب"
        }

        require(password.isNotBlank()) {
            "كلمة المرور مطلوبة"
        }

        val api = NetworkFactory.createApiService(normalizedBaseUrl, null)

        val response = api.login(
            LoginRequest(
                userName = userName.trim(),
                password = password,
                deviceCode = sessionManager.deviceCode,
                deviceName = "Android Phone",
                deviceModel = Build.MODEL ?: "Unknown",
                appVersion = "1.0.0"
            )
        )

        sessionManager.baseUrl = normalizedBaseUrl
        sessionManager.token = response.token
        sessionManager.collectorId = response.collectorId
        sessionManager.collectorName = response.collectorName
        sessionManager.userName = response.userName
        sessionManager.fullName = response.fullName
        sessionManager.deviceCode = response.deviceCode
    }

    private fun normalizeBaseUrl(baseUrl: String): String {
        val trimmed = baseUrl.trim()
        return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }
}
*/