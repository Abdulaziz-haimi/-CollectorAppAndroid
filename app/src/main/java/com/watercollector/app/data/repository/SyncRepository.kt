package com.watercollector.app.data.repository

import androidx.room.withTransaction
import com.watercollector.app.data.local.AppDatabase
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity
import com.watercollector.app.data.local.entities.LocalSubscriberCreditEntity
import com.watercollector.app.data.local.entities.LocalSubscriberEntity
import com.watercollector.app.data.local.entities.LocalSubscriberMeterEntity
import com.watercollector.app.data.remote.NetworkFactory
import com.watercollector.app.data.remote.model.UploadBatchRequest
import com.watercollector.app.data.remote.model.UploadReceiptLineRow
import com.watercollector.app.data.remote.model.UploadReceiptRow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SyncRepository(
    private val database: AppDatabase,
    private val sessionManager: SessionManager,
    private val receiptRepository: ReceiptRepository
) {
    private fun api() = NetworkFactory.createApiService(sessionManager.baseUrl, sessionManager.token)

    suspend fun downloadReceivables(): Result<Unit> = runCatching {
        val response = api().getReceivables(null)
        database.withTransaction {
            database.creditDao().clearAll()
            database.invoiceDao().clearAll()
            database.meterDao().clearAll()
            database.subscriberDao().clearAll()

            database.subscriberDao().insertAll(response.subscribers.map {
                LocalSubscriberEntity(
                    subscriberId = it.subscriberId,
                    subscriberName = it.subscriberName,
                    phoneNumber = it.phoneNumber,
                    address = it.address,
                    primaryMeterId = it.primaryMeterId,
                    primaryMeterNumber = it.primaryMeterNumber,
                    primaryMeterLocation = it.primaryMeterLocation,
                    currentDue = it.currentDue,
                    currentCredit = it.currentCredit,
                    currentBalance = it.currentBalance,
                    lastInvoiceId = it.lastInvoiceId,
                    lastInvoiceNumber = it.lastInvoiceNumber,
                    lastInvoiceDate = it.lastInvoiceDate,
                    lastInvoiceTotal = it.lastInvoiceTotal,
                    lastInvoiceRemaining = it.lastInvoiceRemaining
                )
            })

            database.meterDao().insertAll(response.meters.map {
                LocalSubscriberMeterEntity(
                    subscriberMeterId = it.subscriberMeterId,
                    subscriberId = it.subscriberId,
                    meterId = it.meterId,
                    meterNumber = it.meterNumber,
                    meterType = it.meterType,
                    location = it.location,
                    isActive = it.isActive,
                    isPrimary = it.isPrimary,
                    linkedAt = it.linkedAt
                )
            })

            database.invoiceDao().insertAll(response.openInvoices.map {
                LocalOpenInvoiceEntity(
                    invoiceId = it.invoiceId,
                    subscriberId = it.subscriberId,
                    meterId = it.meterId,
                    invoiceNumber = it.invoiceNumber,
                    invoiceDate = it.invoiceDate,
                    consumption = it.consumption,
                    unitPrice = it.unitPrice,
                    serviceFees = it.serviceFees,
                    arrears = it.arrears,
                    totalAmount = it.totalAmount,
                    grandTotal = it.grandTotal,
                    paidTotal = it.paidTotal,
                    remaining = it.remaining,
                    status = it.status,
                    notes = it.notes
                )
            })

            database.creditDao().insertAll(response.openCredits.map {
                LocalSubscriberCreditEntity(
                    creditId = it.creditId,
                    subscriberId = it.subscriberId,
                    paymentId = it.paymentId,
                    receiptId = it.receiptId,
                    meterId = it.meterId,
                    creditDate = it.creditDate,
                    amountTotal = it.amountTotal,
                    amountRemaining = it.amountRemaining,
                    notes = it.notes
                )
            })
        }
        sessionManager.lastExportedAt = response.summary.exportedAt
    }

    suspend fun uploadPendingDrafts(): Result<String> = runCatching {
        val pending = receiptRepository.getPendingWithLines()
        if (pending.isEmpty()) return@runCatching "لا توجد مسودات Pending للرفع"

        val receipts = pending.mapIndexed { index, draftWithLines ->
            UploadReceiptRow(
                rowNo = index + 1,
                localPaymentGuid = draftWithLines.draft.localPaymentGuid,
                localReceiptNo = draftWithLines.draft.localReceiptNo,
                subscriberId = draftWithLines.draft.subscriberId,
                paymentDate = draftWithLines.draft.paymentDate,
                totalReceived = draftWithLines.draft.totalReceived,
                paymentMethod = draftWithLines.draft.paymentMethod,
                notes = draftWithLines.draft.notes
            )
        }

        val lines = pending.flatMapIndexed { index, draftWithLines ->
            draftWithLines.lines.map {
                UploadReceiptLineRow(
                    receiptRowNo = index + 1,
                    invoiceId = it.invoiceId,
                    appliedAmount = it.appliedAmount,
                    applicationType = it.applicationType,
                    notes = it.notes
                )
            }
        }

        val response = api().uploadBatch(
            UploadBatchRequest(
                deviceCode = sessionManager.deviceCode,
                deviceName = "Android Phone",
                deviceModel = android.os.Build.MODEL,
                appVersion = "1.0.0",
                receipts = receipts,
                lines = lines
            )
        )

        val nowIso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(Date())
        response.rowResults.forEach { row ->
            val localReceiptId = receiptRepository.findLocalReceiptIdByGuid(row.localPaymentGuid) ?: return@forEach
            val syncStatus = if (row.saveStatus.equals("Duplicate", true)) "Duplicate" else "Sent"
            receiptRepository.updateSyncState(
                localReceiptId = localReceiptId,
                syncStatus = syncStatus,
                syncBatchRef = response.syncBatchId.toString(),
                serverImportId = row.importId,
                serverStatus = row.saveStatus,
                rejectedReason = null,
                updatedAt = nowIso,
                sentAt = nowIso
            )
        }

        "تم الرفع. SyncBatchID=${response.syncBatchId}, Inserted=${response.insertedCount}, Duplicate=${response.duplicateCount}"
    }

    suspend fun downloadImportDecisions(): Result<String> = runCatching {
        val response = api().getImportDecisions(
            deviceCode = sessionManager.deviceCode,
            changedAfter = sessionManager.lastDecisionSyncAt
        )

        val nowIso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(Date())
        response.decisions.forEach { decision ->
            val localReceiptId = receiptRepository.findLocalReceiptIdByGuid(decision.localPaymentGuid) ?: return@forEach
            val syncStatus = if (decision.importStatus.equals("Approved", true)) "Approved" else "Rejected"
            receiptRepository.updateSyncState(
                localReceiptId = localReceiptId,
                syncStatus = syncStatus,
                syncBatchRef = decision.syncBatchId.toString(),
                serverImportId = decision.importId,
                serverStatus = decision.importStatus,
                rejectedReason = decision.rejectedReason,
                updatedAt = nowIso,
                sentAt = null
            )
        }

        if (!response.maxChangedAt.isNullOrBlank()) {
            sessionManager.lastDecisionSyncAt = response.maxChangedAt
        }

        "تم تنزيل ${response.decisions.size} قرار/قرارات"
    }

    suspend fun fullSync(): Result<String> = runCatching {
        val parts = mutableListOf<String>()
        parts += downloadReceivables().getOrThrow().let { "1) تم تنزيل المستحقات" }
        parts += uploadPendingDrafts().getOrThrow()
        parts += downloadImportDecisions().getOrThrow()
        parts.joinToString("\n")
    }
}
