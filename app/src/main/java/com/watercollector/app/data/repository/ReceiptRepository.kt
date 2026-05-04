package com.watercollector.app.data.repository

import com.watercollector.app.data.local.dao.ReceiptDraftDao
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.data.local.entities.LocalReceiptDraftLineEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class SavedReceiptInfo(
    val localReceiptId: Long,
    val localPaymentGuid: String,
    val localReceiptNo: String,
    val paymentDate: String,
    val createdAt: String
)

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
    ): SavedReceiptInfo {
        require(subscriberId > 0) {
            "رقم المشترك غير صحيح"
        }

        require(collectorId > 0) {
            "رقم المحصل غير صحيح"
        }

        require(totalReceived > 0) {
            "مبلغ التحصيل يجب أن يكون أكبر من صفر"
        }

        val validAllocations = allocations.filter { (_, amount) ->
            amount > 0
        }

        require(validAllocations.isNotEmpty()) {
            "لا توجد مبالغ صالحة للحفظ"
        }

        val now = Date()

        val dateOnly = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).format(now)

        val nowIso = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale.US
        ).format(now)

        val receiptNo = "MOB-${
            SimpleDateFormat(
                "yyyyMMddHHmmss",
                Locale.US
            ).format(now)
        }"

        val guid = UUID.randomUUID()
            .toString()
            .replace("-", "")

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

        val lines = validAllocations.map { (invoiceId, amount) ->
            LocalReceiptDraftLineEntity(
                localReceiptId = 0,
                invoiceId = invoiceId,
                appliedAmount = amount,
                applicationType = if (invoiceId == null) {
                    "AdvanceCredit"
                } else {
                    "InvoicePayment"
                },
                notes = null
            )
        }

        val localReceiptId = receiptDraftDao.insertDraftWithLines(
            draft = draft,
            lines = lines
        )

        return SavedReceiptInfo(
            localReceiptId = localReceiptId,
            localPaymentGuid = guid,
            localReceiptNo = receiptNo,
            paymentDate = dateOnly,
            createdAt = nowIso
        )
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
        localReceiptId = localReceiptId,
        syncStatus = syncStatus,
        syncBatchRef = syncBatchRef,
        serverImportId = serverImportId,
        serverStatus = serverStatus,
        rejectedReason = rejectedReason,
        updatedAt = updatedAt,
        sentAt = sentAt
    )

    suspend fun findLocalReceiptIdByGuid(
        guid: String
    ) = receiptDraftDao.findLocalReceiptIdByGuid(guid)
}