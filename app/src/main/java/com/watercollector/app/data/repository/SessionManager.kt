package com.watercollector.app.data.repository

import android.content.Context
import java.util.UUID

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("collector_session", Context.MODE_PRIVATE)

    var token: String?
        get() = prefs.getString("token", null)
        set(value) = prefs.edit().putString("token", value).apply()

    var baseUrl: String
        get() = prefs.getString("baseUrl", "http://10.0.2.2:5099/") ?: "http://10.0.2.2:5099/"
        set(value) = prefs.edit().putString("baseUrl", value).apply()

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

    fun isLoggedIn(): Boolean = !token.isNullOrBlank() && collectorId > 0

    fun clear() {
        prefs.edit().clear().apply()
    }
}
