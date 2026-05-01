package com.watercollector.app.data.repository

import com.watercollector.app.data.local.dao.ReceiptDraftDao
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.data.local.entities.LocalReceiptDraftLineEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ReceiptRepository(
    private val receiptDraftDao: ReceiptDraftDao
) {
    fun observeAll() = receiptDraftDao.observeAll()
    fun observePending() = receiptDraftDao.observePending()
    suspend fun getPendingWithLines() = receiptDraftDao.getPendingWithLines()

    suspend fun createDraft(
        subscriberId: Int,
        collectorId: Int,
        totalReceived: Double,
        paymentMethod: String,
        notes: String?,
        allocations: List<Pair<Int?, Double>>
    ): Long {
        val now = Date()
        val dateOnly = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(now)
        val nowIso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(now)
        val receiptNo = "MOB-${SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(now)}"
        val guid = UUID.randomUUID().toString().replace("-", "")

        val draft = LocalReceiptDraftEntity(
            localPaymentGuid = guid,
            localReceiptNo = receiptNo,
            subscriberId = subscriberId,
            collectorId = collectorId,
            paymentDate = dateOnly,
            totalReceived = totalReceived,
            paymentMethod = paymentMethod,
            notes = notes,
            syncStatus = "Pending",
            syncBatchRef = null,
            serverImportId = null,
            serverStatus = null,
            rejectedReason = null,
            createdAt = nowIso,
            updatedAt = null,
            sentAt = null
        )

        val lines = allocations.filter { it.second > 0 }.map { (invoiceId, amount) ->
            LocalReceiptDraftLineEntity(
                localReceiptId = 0,
                invoiceId = invoiceId,
                appliedAmount = amount,
                applicationType = if (invoiceId == null) "AdvanceCredit" else "InvoicePayment",
                notes = null
            )
        }

        return receiptDraftDao.insertDraftWithLines(draft, lines)
    }

    suspend fun updateSyncState(
        localReceiptId: Long,
        syncStatus: String,
        syncBatchRef: String?,
        serverImportId: Int?,
        serverStatus: String?,
        rejectedReason: String?,
        updatedAt: String?,
        sentAt: String?
    ) = receiptDraftDao.updateSyncState(
        localReceiptId,
        syncStatus,
        syncBatchRef,
        serverImportId,
        serverStatus,
        rejectedReason,
        updatedAt,
        sentAt
    )

    suspend fun findLocalReceiptIdByGuid(guid: String) = receiptDraftDao.findLocalReceiptIdByGuid(guid)
}
