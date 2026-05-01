package com.watercollector.app.ui.newreceipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity
import com.watercollector.app.data.repository.ReceiptRepository
import com.watercollector.app.data.repository.SubscriberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewReceiptViewModel(
    private val subscriberRepository: SubscriberRepository,
    private val receiptRepository: ReceiptRepository,
    private val collectorId: Int
) : ViewModel() {
    private val _subscriberId = MutableStateFlow(0)
    val subscriberId: StateFlow<Int> = _subscriberId.asStateFlow()

    private val _invoices = MutableStateFlow<List<LocalOpenInvoiceEntity>>(emptyList())
    val invoices: StateFlow<List<LocalOpenInvoiceEntity>> = _invoices.asStateFlow()

    private val _saveMessage = MutableStateFlow<String?>(null)
    val saveMessage: StateFlow<String?> = _saveMessage.asStateFlow()

    fun load(subscriberId: Int) {
        _subscriberId.value = subscriberId
        viewModelScope.launch {
            _invoices.value = subscriberRepository.getOpenInvoicesForSubscriber(subscriberId)
        }
    }

    fun autoDistribute(totalAmount: Double): Map<Int, Double> {
        var remaining = totalAmount
        val result = linkedMapOf<Int, Double>()
        for (invoice in _invoices.value.sortedBy { it.invoiceDate }) {
            if (remaining <= 0.0) break
            val amount = minOf(remaining, invoice.remaining)
            if (amount > 0.0) {
                result[invoice.invoiceId] = amount
                remaining -= amount
            }
        }
        return result
    }

    fun saveDraft(totalReceived: Double, paymentMethod: String, notes: String?, allocations: Map<Int, Double>) {
        viewModelScope.launch {
            if (totalReceived <= 0.0) {
                _saveMessage.value = "المبلغ يجب أن يكون أكبر من صفر"
                return@launch
            }

            val lines = mutableListOf<Pair<Int?, Double>>()
            var allocatedTotal = 0.0
            for (invoice in _invoices.value) {
                val amount = allocations[invoice.invoiceId] ?: 0.0
                if (amount < 0.0) {
                    _saveMessage.value = "لا يمكن إدخال قيمة سالبة"
                    return@launch
                }
                if (amount > invoice.remaining + 0.0001) {
                    _saveMessage.value = "لا يمكن تطبيق مبلغ أكبر من المتبقي على الفاتورة ${invoice.invoiceNumber ?: invoice.invoiceId}"
                    return@launch
                }
                if (amount > 0.0) {
                    lines += invoice.invoiceId to amount
                    allocatedTotal += amount
                }
            }

            if (allocatedTotal - totalReceived > 0.0001) {
                _saveMessage.value = "مجموع التوزيع أكبر من إجمالي التحصيل"
                return@launch
            }

            val advanceCredit = totalReceived - allocatedTotal
            if (advanceCredit > 0.0001) lines += null to advanceCredit

            receiptRepository.createDraft(
                subscriberId = _subscriberId.value,
                collectorId = collectorId,
                totalReceived = totalReceived,
                paymentMethod = paymentMethod,
                notes = notes,
                allocations = lines
            )
            _saveMessage.value = "تم حفظ المسودة بنجاح"
        }
    }
}

class NewReceiptViewModelFactory(
    private val subscriberRepository: SubscriberRepository,
    private val receiptRepository: ReceiptRepository,
    private val collectorId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewReceiptViewModel(subscriberRepository, receiptRepository, collectorId) as T
    }
}
