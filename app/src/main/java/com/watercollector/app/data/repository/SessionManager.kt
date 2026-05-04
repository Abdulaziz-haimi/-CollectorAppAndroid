package com.watercollector.app.data.repository

import android.content.Context
import java.util.UUID

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("collector_session", Context.MODE_PRIVATE)

    companion object {
        // غيّر هذا إلى IP اللابتوب والمنفذ الذي يعمل عليه الـ Backend عبر HTTPS
        const val DEFAULT_BASE_URL = "https://192.168.0.135:8443/"

        fun normalizeBaseUrl(value: String?): String {
            val trimmed = value?.trim().orEmpty()

            if (trimmed.isBlank()) {
                return DEFAULT_BASE_URL
            }

            val withScheme = when {
                trimmed.startsWith("https://", ignoreCase = true) -> trimmed

                trimmed.startsWith("http://", ignoreCase = true) ->
                    trimmed.replaceFirst(
                        "http://",
                        "https://",
                        ignoreCase = true
                    )

                else -> "https://$trimmed"
            }

            return if (withScheme.endsWith("/")) withScheme else "$withScheme/"
        }
    }

    var token: String?
        get() = prefs.getString("token", null)
        set(value) = prefs.edit().putString("token", value).apply()

    var baseUrl: String
        get() = prefs.getString("baseUrl", DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
        set(value) {
            prefs.edit()
                .putString("baseUrl", normalizeBaseUrl(value))
                .apply()
        }

    var collectorId: Int
        get() = prefs.getInt("collectorId", 0)
        set(value) = prefs.edit().putInt("collectorId", value).apply()

    var collectorName: String?
        get() = prefs.getString("collectorName", null)
        set(value) = prefs.edit().putString("collectorName", value).apply()

    var userName: String?
        get() = prefs.getString("userName", null)
        set(value) = prefs.edit().putString("userName", value).apply()

    var fullName: String?
        get() = prefs.getString("fullName", null)
        set(value) = prefs.edit().putString("fullName", value).apply()

    var deviceCode: String
        get() {
            val existing = prefs.getString("deviceCode", null)
            if (!existing.isNullOrBlank()) return existing

            val generated = UUID.randomUUID().toString().replace("-", "")
            prefs.edit().putString("deviceCode", generated).apply()
            return generated
        }
        set(value) = prefs.edit().putString("deviceCode", value).apply()

    var lastExportedAt: String?
        get() = prefs.getString("lastExportedAt", null)
        set(value) = prefs.edit().putString("lastExportedAt", value).apply()

    var lastDecisionSyncAt: String?
        get() = prefs.getString("lastDecisionSyncAt", null)
        set(value) = prefs.edit().putString("lastDecisionSyncAt", value).apply()
    var offlineUserName: String?
        get() = prefs.getString("offlineUserName", null)
        set(value) = prefs.edit().putString("offlineUserName", value).apply()

    var offlinePasswordHash: String?
        get() = prefs.getString("offlinePasswordHash", null)
        set(value) = prefs.edit().putString("offlinePasswordHash", value).apply()

    var offlineLoginEnabled: Boolean
        get() = prefs.getBoolean("offlineLoginEnabled", false)
        set(value) = prefs.edit().putBoolean("offlineLoginEnabled", value).apply()

    var printerMacAddress: String?
        get() = prefs.getString("printerMacAddress", null)
        set(value) = prefs.edit().putString("printerMacAddress", value).apply()

    fun isLoggedIn(): Boolean = !token.isNullOrBlank() && collectorId > 0

    fun clear() {
        // نحافظ على رابط الخادم ورمز الجهاز بعد تسجيل الخروج
        val savedBaseUrl = baseUrl
        val savedDeviceCode = deviceCode

        prefs.edit()
            .clear()
            .putString("baseUrl", savedBaseUrl)
            .putString("deviceCode", savedDeviceCode)
            .apply()
    }

}