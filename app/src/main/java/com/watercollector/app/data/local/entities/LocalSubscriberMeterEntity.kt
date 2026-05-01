package com.watercollector.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_subscriber_meters")
data class LocalSubscriberMeterEntity(
    @PrimaryKey val subscriberMeterId: Int,
    val subscriberId: Int,
    val meterId: Int,
    val meterNumber: String,
    val meterType: String?,
    val location: String?,
    val isActive: Boolean,
    val isPrimary: Boolean,
    val linkedAt: String?
)
