package com.watercollector.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_subscriber_credits")
data class LocalSubscriberCreditEntity(
    @PrimaryKey val creditId: Int,
    val subscriberId: Int,
    val paymentId: Int?,
    val receiptId: Int?,
    val meterId: Int?,
    val creditDate: String,
    val amountTotal: Double,
    val amountRemaining: Double,
    val notes: String?
)
