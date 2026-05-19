package com.watercollector.app.data.repository

import android.os.Build
import com.watercollector.app.data.local.dao.CachedLoginDao
import com.watercollector.app.data.local.entities.CachedLoginEntity
import com.watercollector.app.data.remote.NetworkFactory
import com.watercollector.app.data.remote.model.LoginRequest
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.MessageDigest
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

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
            // هنا السيرفر وصل ورفض الطلب، لذلك لا ندخل Offline
            Result.failure(Exception(readApiError(e)))

        } catch (e: SSLHandshakeException) {
            Result.failure(
                Exception(
                    "فشل الاتصال بسبب شهادة HTTPS.\n" +
                            "تأكد أن water3_root_ca.cer موجودة داخل التطبيق، وأن شهادة السيرفر تحتوي IP الحالي داخل SAN."
                )
            )

        } catch (e: SSLPeerUnverifiedException) {
            Result.failure(
                Exception(
                    "فشل التحقق من شهادة HTTPS.\n" +
                            "غالبًا شهادة السيرفر لا تحتوي نفس IP المستخدم في الرابط:\n$normalizedBaseUrl"
                )
            )

        } catch (e: SSLException) {
            Result.failure(
                Exception(
                    "خطأ SSL/HTTPS: ${e.message ?: "فشل التحقق من الاتصال الآمن"}"
                )
            )

        } catch (e: UnknownHostException) {
            loginOfflineOrConnectionError(
                normalizedBaseUrl,
                cleanUserName,
                password,
                "تعذر الوصول إلى عنوان السيرفر. تأكد من الرابط:\n$normalizedBaseUrl"
            )

        } catch (e: ConnectException) {
            loginOfflineOrConnectionError(
                normalizedBaseUrl,
                cleanUserName,
                password,
                "تعذر الاتصال بالسيرفر. تأكد أن برنامج Water3 شغال وأن الجوال على نفس الشبكة."
            )

        } catch (e: SocketTimeoutException) {
            loginOfflineOrConnectionError(
                normalizedBaseUrl,
                cleanUserName,
                password,
                "انتهت مهلة الاتصال بالسيرفر. تأكد من الشبكة والجدار الناري والمنفذ 8443."
            )

        } catch (e: Exception) {
            loginOfflineOrConnectionError(
                normalizedBaseUrl,
                cleanUserName,
                password,
                "فشل الاتصال بالسيرفر: ${e.javaClass.simpleName} - ${e.message ?: "خطأ غير معروف"}"
            )
        }
    }

    private suspend fun loginOnline(
        baseUrl: String,
        userName: String,
        password: String
    ) {
        val api = NetworkFactory.createApiService(baseUrl, null)

        val response = api.login(
            LoginRequest(
                userName = userName,
                password = password,
                deviceCode = sessionManager.deviceCode,
                deviceName = "Android Phone",
                deviceModel = Build.MODEL ?: "Android",
                appVersion = "1.0.0"
            )
        )

        val responseUserName = response.userName.ifBlank { userName }
        val responseDeviceCode = response.deviceCode.ifBlank { sessionManager.deviceCode }

        sessionManager.baseUrl = baseUrl
        sessionManager.token = response.token
        sessionManager.collectorId = response.collectorId
        sessionManager.collectorName = response.collectorName
        sessionManager.userName = responseUserName
        sessionManager.fullName = response.fullName
        sessionManager.deviceCode = responseDeviceCode

        cachedLoginDao.upsert(
            CachedLoginEntity(
                userName = responseUserName,
                passwordHash = hashPassword(password, responseDeviceCode),
                baseUrl = baseUrl,
                token = response.token,
                collectorId = response.collectorId,
                collectorName = response.collectorName,
                fullName = response.fullName,
                deviceCode = responseDeviceCode
            )
        )
    }

    private suspend fun loginOfflineOrConnectionError(
        baseUrl: String,
        userName: String,
        password: String,
        connectionMessage: String
    ): Result<Unit> {
        val cachedResult = loginOffline(baseUrl, userName, password)

        return if (cachedResult.isSuccess) {
            cachedResult
        } else {
            Result.failure(Exception(connectionMessage))
        }
    }

    private suspend fun loginOffline(
        baseUrl: String,
        userName: String,
        password: String
    ): Result<Unit> {
        val cached = cachedLoginDao.getByUserName(userName)
            ?: return Result.failure(Exception("لا توجد بيانات دخول محفوظة لهذا المستخدم."))

        val expectedHash = hashPassword(password, cached.deviceCode)

        if (cached.passwordHash != expectedHash) {
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

    private fun readApiError(e: HttpException): String {
        val fallback = when (e.code()) {
            400 -> "بيانات تسجيل الدخول غير مكتملة أو غير صحيحة."
            401 -> "فشل تسجيل الدخول: اسم المستخدم أو كلمة المرور غير صحيحة، أو الجهاز غير مصرح له."
            403 -> "ليس لديك صلاحية الدخول من تطبيق المحصل."
            404 -> "رابط تسجيل الدخول غير موجود في السيرفر. تأكد من رابط الخادم."
            500 -> "خطأ داخلي في السيرفر."
            else -> "فشل تسجيل الدخول من السيرفر: HTTP ${e.code()}"
        }

        return try {
            val body = e.response()?.errorBody()?.string()

            if (body.isNullOrBlank()) {
                fallback
            } else {
                val trimmed = body.trim()

                if (trimmed.startsWith("{")) {
                    val json = JSONObject(trimmed)

                    json.optString("message").ifBlank {
                        json.optString("error").ifBlank {
                            json.optString("title").ifBlank {
                                json.optString("detail").ifBlank {
                                    fallback
                                }
                            }
                        }
                    }
                } else {
                    trimmed.ifBlank { fallback }
                }
            }
        } catch (_: Exception) {
            fallback
        }
    }

    private fun hashPassword(password: String, deviceCode: String): String {
        val input = "$deviceCode:$password"
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
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