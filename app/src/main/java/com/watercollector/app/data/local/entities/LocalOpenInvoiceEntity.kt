package com.watercollector.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_open_invoices")
data class LocalOpenInvoiceEntity(
    @PrimaryKey val invoiceId: Int,
    val subscriberId: Int,
    val meterId: Int?,
    val invoiceNumber: String?,
    val invoiceDate: String,
    val consumption: Double,
    val unitPrice: Double,
    val serviceFees: Double,
    val arrears: Double,
    val totalAmount: Double,
    val grandTotal: Double,
    val paidTotal: Double,
    val remaining: Double,
    val status: String?,
    val notes: String?
)
