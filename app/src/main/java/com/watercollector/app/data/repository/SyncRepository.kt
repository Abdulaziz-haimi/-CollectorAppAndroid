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
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SyncRepository(
    private val database: AppDatabase,
    private val sessionManager: SessionManager,
    private val receiptRepository: ReceiptRepository
) {
    private fun api() =
        NetworkFactory.createApiService(
            baseUrl = sessionManager.baseUrl,
            token = sessionManager.token
        )

    suspend fun downloadReceivables(): Result<Unit> {
        return try {
            require(sessionManager.baseUrl.isNotBlank()) {
                "رابط الخادم غير مضبوط"
            }

            require(!sessionManager.token.isNullOrBlank()) {
                "يجب تسجيل الدخول قبل المزامنة"
            }

            val response = api().getReceivables(null)

            database.withTransaction {
                database.creditDao().clearAll()
                database.invoiceDao().clearAll()
                database.meterDao().clearAll()
                database.subscriberDao().clearAll()

                database.subscriberDao().insertAll(
                    response.subscribers.map {
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
                    }
                )

                database.meterDao().insertAll(
                    response.meters.map {
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
                    }
                )

                database.invoiceDao().insertAll(
                    response.openInvoices.map {
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
                    }
                )

                database.creditDao().insertAll(
                    response.openCredits.map {
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
                    }
                )
            }

            sessionManager.lastExportedAt = response.summary.exportedAt

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(
                Exception(
                    "فشل تنزيل المستحقات: ${exception.toReadableMessage()}",
                    exception
                )
            )
        }
    }

    suspend fun uploadPendingDrafts(): Result<String> {
        return try {
            require(sessionManager.baseUrl.isNotBlank()) {
                "رابط الخادم غير مضبوط"
            }

            require(!sessionManager.token.isNullOrBlank()) {
                "يجب تسجيل الدخول قبل رفع التحصيلات"
            }

            val pending = receiptRepository.getPendingWithLines()

            if (pending.isEmpty()) {
                return Result.success("لا توجد تحصيلات معلقة للرفع")
            }

            val receipts = pending.mapIndexed { index, draftWithLines ->
                UploadReceiptRow(
                    rowNo = index + 1,
                    localPaymentGuid = draftWithLines.draft.localPaymentGuid,
                    localReceiptNo = draftWithLines.draft.localReceiptNo,
                    subscriberId = draftWithLines.draft.subscriberId,
                    paymentDate = draftWithLines.draft.paymentDate,
                    totalReceived = draftWithLines.draft.totalReceived,
                    paymentMethod = normalizePaymentMethod(draftWithLines.draft.paymentMethod),
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
                    deviceModel = android.os.Build.MODEL ?: "Unknown",
                    appVersion = "1.0.0",
                    receipts = receipts,
                    lines = lines
                )
            )

            val nowIso = nowIso()

            response.rowResults.forEach { row ->
                val localReceiptId =
                    receiptRepository.findLocalReceiptIdByGuid(row.localPaymentGuid)
                        ?: return@forEach

                val syncStatus = when {
                    row.saveStatus.equals("Duplicate", ignoreCase = true) -> "Duplicate"
                    row.saveStatus.equals("Inserted", ignoreCase = true) -> "Sent"
                    else -> "Sent"
                }

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

            Result.success(
                "تم رفع التحصيلات.\n" +
                        "رقم دفعة المزامنة: ${response.syncBatchId}\n" +
                        "الجديد: ${response.insertedCount}\n" +
                        "المكرر: ${response.duplicateCount}"
            )
        } catch (exception: Exception) {
            Result.failure(
                Exception(
                    "فشل رفع التحصيلات المعلقة: ${exception.toReadableMessage()}",
                    exception
                )
            )
        }
    }

    suspend fun downloadImportDecisions(): Result<String> {
        return try {
            require(sessionManager.baseUrl.isNotBlank()) {
                "رابط الخادم غير مضبوط"
            }

            require(!sessionManager.token.isNullOrBlank()) {
                "يجب تسجيل الدخول قبل تنزيل قرارات الإدارة"
            }

            val response = api().getImportDecisions(
                deviceCode = sessionManager.deviceCode,
                changedAfter = sessionManager.lastDecisionSyncAt
            )

            val nowIso = nowIso()

            response.decisions.forEach { decision ->
                val localReceiptId =
                    receiptRepository.findLocalReceiptIdByGuid(decision.localPaymentGuid)
                        ?: return@forEach

                val syncStatus = if (decision.importStatus.equals("Approved", ignoreCase = true)) {
                    "Approved"
                } else {
                    "Rejected"
                }

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

            Result.success("تم تنزيل ${response.decisions.size} قرار/قرارات")
        } catch (exception: Exception) {
            Result.failure(
                Exception(
                    "فشل تنزيل قرارات الإدارة: ${exception.toReadableMessage()}",
                    exception
                )
            )
        }
    }

    suspend fun fullSync(): Result<String> {
        return try {
            val parts = mutableListOf<String>()

            val uploadResult = uploadPendingDrafts().getOrThrow()
            parts += "1) $uploadResult"

            val decisionsResult = downloadImportDecisions().getOrThrow()
            parts += "2) $decisionsResult"

            downloadReceivables().getOrThrow()
            parts += "3) تم تنزيل المستحقات وتحديث البيانات المحلية"

            Result.success(parts.joinToString("\n"))
        } catch (exception: Exception) {
            Result.failure(
                Exception(
                    "فشلت المزامنة الكاملة: ${exception.toReadableMessage()}",
                    exception
                )
            )
        }
    }

    private fun nowIso(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale.US
        ).format(Date())
    }

    private fun normalizePaymentMethod(method: String): String {
        return when (method.trim().lowercase(Locale.US)) {
            "cash", "نقد", "نقدًا" -> "Cash"
            "transfer", "حوالة" -> "Transfer"
            "wallet", "محفظة" -> "Wallet"
            "cheque", "check", "شيك" -> "Cheque"
            "card", "بطاقة" -> "Card"
            "bank", "تحويل بنكي" -> "Transfer"
            "other", "أخرى" -> "Other"
            else -> method.ifBlank { "Cash" }
        }
    }

    private fun Throwable.toReadableMessage(): String {
        if (this is HttpException) {
            val code = code()
            val rawBody = response()?.errorBody()?.string()

            if (!rawBody.isNullOrBlank()) {
                val serverMessage = extractServerMessage(rawBody)
                return "HTTP $code - $serverMessage"
            }

            return "HTTP $code"
        }

        return message ?: "خطأ غير معروف"
    }

    private fun extractServerMessage(errorBody: String): String {
        return try {
            val json = JSONObject(errorBody)

            when {
                json.has("message") -> json.optString("message")
                json.has("Message") -> json.optString("Message")
                json.has("error") -> json.optString("error")
                json.has("Error") -> json.optString("Error")
                else -> errorBody
            }
        } catch (_: Exception) {
            errorBody
        }
    }
}