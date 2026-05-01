package com.watercollector.app.ui.receipts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.data.repository.ReceiptRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class LocalReceiptsViewModel(repository: ReceiptRepository) : ViewModel() {
    val items: StateFlow<List<LocalReceiptDraftEntity>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class LocalReceiptsViewModelFactory(private val repository: ReceiptRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocalReceiptsViewModel(repository) as T
    }
}
