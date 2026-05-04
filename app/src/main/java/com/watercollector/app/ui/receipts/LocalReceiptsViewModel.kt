package com.watercollector.app.ui.receipts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.data.repository.ReceiptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LocalReceiptsViewModel(
    receiptRepository: ReceiptRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val items: StateFlow<List<LocalReceiptDraftEntity>> =
        combine(
            receiptRepository.observeAll(),
            searchQuery
        ) { receipts, query ->
            filterReceipts(receipts, query)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun setSearchQuery(query: String) {
        searchQuery.value = query.trim()
    }

    private fun filterReceipts(
        receipts: List<LocalReceiptDraftEntity>,
        query: String
    ): List<LocalReceiptDraftEntity> {
        if (query.isBlank()) {
            return receipts
        }

        val cleanQuery = query.trim().lowercase()

        return receipts.filter { receipt ->
            receipt.localReceiptNo.lowercase().contains(cleanQuery) ||
                    receipt.subscriberId.toString().contains(cleanQuery) ||
                    receipt.totalReceived.toString().contains(cleanQuery) ||
                    receipt.paymentMethod.lowercase().contains(cleanQuery) ||
                    receipt.syncStatus.lowercase().contains(cleanQuery) ||
                    receipt.paymentDate.lowercase().contains(cleanQuery)
        }
    }
}

class LocalReceiptsViewModelFactory(
    private val repository: ReceiptRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(LocalReceiptsViewModel::class.java)) {
            return LocalReceiptsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}