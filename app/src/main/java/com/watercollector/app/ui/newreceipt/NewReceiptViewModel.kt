package com.watercollector.app.ui.newreceipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity
import com.watercollector.app.data.repository.ReceiptRepository
import com.watercollector.app.data.repository.SubscriberRepository
import com.watercollector.app.printing.ReceiptPrintData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewReceiptViewModel(
    private val subscriberRepository: SubscriberRepository,
    private val receiptRepository: ReceiptRepository,
    private val collectorId: Int,
    private val collectorName: String
) : ViewModel() {

    private val _subscriberId = MutableStateFlow(0)
    val subscriberId: StateFlow<Int> = _subscriberId.asStateFlow()

    private val _invoices = MutableStateFlow<List<LocalOpenInvoiceEntity>>(emptyList())
    val invoices: StateFlow<List<LocalOpenInvoiceEntity>> = _invoices.asStateFlow()

    private val _saveMessage = MutableSharedFlow<String>()
    val saveMessage: SharedFlow<String> = _saveMessage.asSharedFlow()

    private val _printRequest = MutableSharedFlow<ReceiptPrintData>()
    val printRequest: SharedFlow<ReceiptPrintData> = _printRequest.asSharedFlow()

    fun load(subscriberId: Int) {
        _subscriberId.value = subscriberId

        viewModelScope.launch {
            try {
                _invoices.value = subscriberRepository.getOpenInvoicesForSubscriber(subscriberId)
            } catch (exception: Exception) {
                _saveMessage.emit(
                    exception.message ?: "فشل تحميل فواتير المشترك"
                )
            }
        }
    }

    fun autoDistribute(totalAmount: Double): Map<Int, Double> {
        if (totalAmount <= 0.0) return emptyMap()

        var remaining = totalAmount
        val result = linkedMapOf<Int, Double>()

        for (invoice in _invoices.value.sortedBy { it.invoiceDate }) {
            if (remaining <= 0.0) break

            val amount = minOf(
                remaining,
                invoice.remaining
            )

            if (amount > 0.0) {
                result[invoice.invoiceId] = amount
                remaining -= amount
            }
        }

        return result
    }

    fun saveDraft(
        totalReceived: Double,
        paymentMethod: String,
        notes: String?,
        allocations: Map<Int, Double>
    ) {
        viewModelScope.launch {
            try {
                if (totalReceived <= 0.0) {
                    _saveMessage.emit("المبلغ يجب أن يكون أكبر من صفر")
                    return@launch
                }

                if (collectorId <= 0) {
                    _saveMessage.emit("بيانات المحصل غير صحيحة. سجل الدخول مرة أخرى.")
                    return@launch
                }

                val lines = mutableListOf<Pair<Int?, Double>>()
                var allocatedTotal = 0.0

                for (invoice in _invoices.value) {
                    val amount = allocations[invoice.invoiceId] ?: 0.0

                    if (amount < 0.0) {
                        _saveMessage.emit("لا يمكن إدخال قيمة سالبة")
                        return@launch
                    }

                    if (amount > invoice.remaining + 0.0001) {
                        val invoiceLabel = invoice.invoiceNumber ?: invoice.invoiceId.toString()

                        _saveMessage.emit(
                            "لا يمكن تطبيق مبلغ أكبر من المتبقي على الفاتورة $invoiceLabel"
                        )
                        return@launch
                    }

                    if (amount > 0.0) {
                        lines += invoice.invoiceId to amount
                        allocatedTotal += amount
                    }
                }

                if (allocatedTotal - totalReceived > 0.0001) {
                    _saveMessage.emit("مجموع التوزيع أكبر من إجمالي التحصيل")
                    return@launch
                }

                val advanceCredit = totalReceived - allocatedTotal

                if (advanceCredit > 0.0001) {
                    lines += null to advanceCredit
                }

                if (lines.isEmpty()) {
                    _saveMessage.emit("لا توجد مبالغ صالحة للحفظ")
                    return@launch
                }

                val saved = receiptRepository.createDraft(
                    subscriberId = _subscriberId.value,
                    collectorId = collectorId,
                    totalReceived = totalReceived,
                    paymentMethod = paymentMethod.ifBlank { "Cash" },
                    notes = notes,
                    allocations = lines
                )

                val subscriber = subscriberRepository.getSubscriberById(_subscriberId.value)

                _saveMessage.emit("تم حفظ التحصيل محليًا بنجاح")

                _printRequest.emit(
                    ReceiptPrintData(
                        receiptNo = saved.localReceiptNo,
                        dateTime = saved.createdAt,
                        collectorName = collectorName,
                        subscriberId = _subscriberId.value,
                        subscriberName = subscriber?.subscriberName ?: "غير معروف",
                        amount = totalReceived,
                        paymentMethod = paymentMethod.ifBlank { "Cash" },
                        notes = notes
                    )
                )
            } catch (exception: Exception) {
                _saveMessage.emit(
                    exception.message ?: "فشل حفظ التحصيل"
                )
            }
        }
    }
}

class NewReceiptViewModelFactory(
    private val subscriberRepository: SubscriberRepository,
    private val receiptRepository: ReceiptRepository,
    private val collectorId: Int,
    private val collectorName: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(NewReceiptViewModel::class.java)) {
            return NewReceiptViewModel(
                subscriberRepository = subscriberRepository,
                receiptRepository = receiptRepository,
                collectorId = collectorId,
                collectorName = collectorName
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}