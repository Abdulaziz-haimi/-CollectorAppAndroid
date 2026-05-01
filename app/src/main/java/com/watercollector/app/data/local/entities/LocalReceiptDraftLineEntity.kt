package com.watercollector.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_receipt_draft_lines")
data class LocalReceiptDraftLineEntity(
    @PrimaryKey(autoGenerate = true) val localReceiptLineId: Long = 0,
    val localReceiptId: Long,
    val invoiceId: Int?,
    val appliedAmount: Double,
    val applicationType: String,
    val notes: String?
)
