package com.watercollector.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_receipt_drafts")
data class LocalReceiptDraftEntity(
    @PrimaryKey(autoGenerate = true) val localReceiptId: Long = 0,
    val localPaymentGuid: String,
    val localReceiptNo: String,
    val subscriberId: Int,
    val collectorId: Int,
    val paymentDate: String,
    val totalReceived: Double,
    val paymentMethod: String,
    val notes: String?,
    val syncStatus: String,
    val syncBatchRef: String?,
    val serverImportId: Int?,
    val serverStatus: String?,
    val rejectedReason: String?,
    val createdAt: String,
    val updatedAt: String?,
    val sentAt: String?
)
