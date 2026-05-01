package com.watercollector.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_subscribers")
data class LocalSubscriberEntity(
    @PrimaryKey val subscriberId: Int,
    val subscriberName: String,
    val phoneNumber: String?,
    val address: String?,
    val primaryMeterId: Int?,
    val primaryMeterNumber: String?,
    val primaryMeterLocation: String?,
    val currentDue: Double,
    val currentCredit: Double,
    val currentBalance: Double,
    val lastInvoiceId: Int?,
    val lastInvoiceNumber: String?,
    val lastInvoiceDate: String?,
    val lastInvoiceTotal: Double?,
    val lastInvoiceRemaining: Double?
)
