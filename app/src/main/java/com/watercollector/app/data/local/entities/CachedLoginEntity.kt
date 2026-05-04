package com.watercollector.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_login")
data class CachedLoginEntity(
    @PrimaryKey
    val userName: String,
    val passwordHash: String,
    val baseUrl: String,
    val token: String,
    val collectorId: Int,
    val collectorName: String?,
    val fullName: String?,
    val deviceCode: String,
    val cachedAt: Long = System.currentTimeMillis()
)