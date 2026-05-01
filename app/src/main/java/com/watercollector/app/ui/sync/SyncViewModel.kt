package com.watercollector.app.ui.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.watercollector.app.data.repository.SyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SyncViewModel(private val syncRepository: SyncRepository) : ViewModel() {
    private val _resultText = MutableStateFlow("")
    val resultText: StateFlow<String> = _resultText.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun downloadReceivables() = launchWork { syncRepository.downloadReceivables().getOrThrow(); "تم تنزيل المستحقات إلى الهاتف بنجاح" }
    fun uploadPending() = launchWork { syncRepository.uploadPendingDrafts().getOrThrow() }
    fun downloadDecisions() = launchWork { syncRepository.downloadImportDecisions().getOrThrow() }
    fun fullSync() = launchWork { syncRepository.fullSync().getOrThrow() }

    private fun launchWork(block: suspend () -> String) {
        viewModelScope.launch {
            _loading.value = true
            runCatching { block() }
                .onSuccess { _resultText.value = it }
                .onFailure { _resultText.value = "فشل العملية: ${it.message}" }
            _loading.value = false
        }
    }
}

class SyncViewModelFactory(private val syncRepository: SyncRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SyncViewModel(syncRepository) as T
    }
}
