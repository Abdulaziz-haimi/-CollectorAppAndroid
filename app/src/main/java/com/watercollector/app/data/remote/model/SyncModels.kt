package com.watercollector.app.data.remote.model

data class ReceivablesExportResponse(
    val summary: SyncExportSummary,
    val subscribers: List<ReceivableSubscriber>,
    val meters: List<ReceivableMeter>,
    val openInvoices: List<ReceivableInvoice>,
    val openCredits: List<ReceivableCredit>
)

data class SyncExportSummary(
    val collectorId: Int,
    val asOfDate: String,
    val exportedAt: String,
    val subscribersCount: Int,
    val openInvoicesCount: Int,
    val openCreditsCount: Int
)

data class ReceivableSubscriber(
    val subscriberId: Int,
    val subscriberName: String,
    val phoneNumber: String?,
    val address: String?,
    val accountId: Int?,
    val tariffPlanId: Int?,
    val isActive: Boolean,
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

data class ReceivableMeter(
    val subscriberMeterId: Int,
    val subscriberId: Int,
    val meterId: Int,
    val meterNumber: String,
    val meterType: String?,
    val location: String?,
    val isActive: Boolean,
    val isPrimary: Boolean,
    val linkedAt: String?
)

data class ReceivableInvoice(
    val invoiceId: Int,
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

data class ReceivableCredit(
    val creditId: Int,
    val subscriberId: Int,
    val paymentId: Int?,
    val receiptId: Int?,
    val meterId: Int?,
    val creditDate: String,
    val amountTotal: Double,
    val amountRemaining: Double,
    val notes: String?
)

data class UploadBatchRequest(
    val deviceCode: String,
    val deviceName: String?,
    val deviceModel: String?,
    val appVersion: String?,
    val autoCreateDevice: Boolean = true,
    val receipts: List<UploadReceiptRow>,
    val lines: List<UploadReceiptLineRow>
)

data class UploadReceiptRow(
    val rowNo: Int,
    val localPaymentGuid: String,
    val localReceiptNo: String,
    val subscriberId: Int,
    val paymentDate: String,
    val totalReceived: Double,
    val paymentMethod: String,
    val notes: String?
)

data class UploadReceiptLineRow(
    val receiptRowNo: Int,
    val invoiceId: Int?,
    val appliedAmount: Double,
    val applicationType: String,
    val notes: String?
)

data class UploadBatchResponse(
    val syncBatchId: Int,
    val deviceId: Int,
    val totalRows: Int,
    val insertedCount: Int,
    val duplicateCount: Int,
    val batchStatus: String,
    val rowResults: List<UploadRowResult>
)

data class UploadRowResult(
    val rowNo: Int,
    val localPaymentGuid: String,
    val importId: Int?,
    val saveStatus: String
)

data class ImportDecisionsResponse(
    val decisions: List<ImportDecisionItem>,
    val maxChangedAt: String?
)

data class ImportDecisionItem(
    val importId: Int,
    val syncBatchId: Int,
    val localPaymentGuid: String,
    val localReceiptNo: String,
    val importStatus: String,
    val approvedReceiptId: Int?,
    val receiptNumber: String?,
    val approvedAt: String?,
    val approvedByUserId: Int?,
    val approvedByUserName: String?,
    val rejectedReason: String?,
    val createdAt: String,
    val changedAt: String
)
