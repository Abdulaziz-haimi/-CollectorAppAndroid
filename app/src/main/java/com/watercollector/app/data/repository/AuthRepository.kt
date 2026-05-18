package com.watercollector.app.data.repository

import android.os.Build
import com.watercollector.app.data.local.dao.CachedLoginDao
import com.watercollector.app.data.local.entities.CachedLoginEntity
import com.watercollector.app.data.remote.NetworkFactory
import com.watercollector.app.data.remote.model.LoginRequest
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.MessageDigest
import javax.net.ssl.SSLHandshakeException

class AuthRepository(
    private val sessionManager: SessionManager,
    private val cachedLoginDao: CachedLoginDao
) {
    suspend fun login(baseUrl: String, userName: String, password: String): Result<Unit> {
        val normalizedBaseUrl = SessionManager.normalizeBaseUrl(baseUrl)
        val cleanUserName = userName.trim()

        if (cleanUserName.isBlank() || password.isBlank()) {
            return Result.failure(Exception("اسم المستخدم وكلمة المرور مطلوبان."))
        }

        return try {
            loginOnline(normalizedBaseUrl, cleanUserName, password)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("فشل تسجيل الدخول من السيرفر: HTTP ${e.code()}"))
        } catch (e: SSLHandshakeException) {
            Result.failure(Exception("فشل الاتصال بسبب شهادة HTTPS. ثبّت شهادة Water3Api.cer داخل التطبيق أو على الجوال."))
        } catch (e: UnknownHostException) {
            Result.failure(Exception("تعذر الوصول إلى عنوان السيرفر. تأكد من الرابط: $normalizedBaseUrl"))
        } catch (e: ConnectException) {
            Result.failure(Exception("تعذر الاتصال بالسيرفر. تأكد أن البرنامج شغال وأن الجوال على نفس الشبكة."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("انتهت مهلة الاتصال بالسيرفر. تأكد من الشبكة والجدار الناري."))
        } catch (e: Exception) {
            val cachedResult = loginOffline(normalizedBaseUrl, cleanUserName, password)

            if (cachedResult.isSuccess) {
                cachedResult
            } else {
                Result.failure(Exception("فشل الاتصال بالسيرفر: ${e.javaClass.simpleName} - ${e.message ?: "خطأ غير معروف"}"))
            }
        }
    }

    private suspend fun loginOnline(baseUrl: String, userName: String, password: String) {
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

        cachedLoginDao.upsert(
            CachedLoginEntity(
                userName = response.userName,
                passwordHash = hashPassword(password),
                baseUrl = baseUrl,
                token = response.token,
                collectorId = response.collectorId,
                collectorName = response.collectorName,
                fullName = response.fullName,
                deviceCode = response.deviceCode
            )
        )
    }

    private suspend fun loginOffline(
        baseUrl: String,
        userName: String,
        password: String
    ): Result<Unit> {
        val cached = cachedLoginDao.getByUserName(userName)
            ?: return Result.failure(Exception("لا توجد بيانات دخول محفوظة لهذا المستخدم."))

        if (cached.passwordHash != hashPassword(password)) {
            return Result.failure(Exception("كلمة المرور غير صحيحة للبيانات المحفوظة."))
        }

        sessionManager.baseUrl = cached.baseUrl.ifBlank { baseUrl }
        sessionManager.token = cached.token
        sessionManager.collectorId = cached.collectorId
        sessionManager.collectorName = cached.collectorName
        sessionManager.userName = cached.userName
        sessionManager.fullName = cached.fullName
        sessionManager.deviceCode = cached.deviceCode

        return Result.success(Unit)
    }

    private fun hashPassword(password: String): String {
        val input = "${sessionManager.deviceCode}:$password"
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
/*package com.watercollector.app.data.repository

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
*/