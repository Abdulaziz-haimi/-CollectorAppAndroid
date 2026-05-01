package com.watercollector.app.ui.subscriberdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity
import com.watercollector.app.data.local.entities.LocalSubscriberCreditEntity
import com.watercollector.app.data.local.entities.LocalSubscriberEntity
import com.watercollector.app.data.local.entities.LocalSubscriberMeterEntity
import com.watercollector.app.data.repository.SubscriberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriberDetailsViewModel(
    private val repository: SubscriberRepository
) : ViewModel() {
    private val _subscriber = MutableStateFlow<LocalSubscriberEntity?>(null)
    val subscriber: StateFlow<LocalSubscriberEntity?> = _subscriber.asStateFlow()
    private val _invoices = MutableStateFlow<List<LocalOpenInvoiceEntity>>(emptyList())
    val invoices: StateFlow<List<LocalOpenInvoiceEntity>> = _invoices.asStateFlow()
    private val _meters = MutableStateFlow<List<LocalSubscriberMeterEntity>>(emptyList())
    val meters: StateFlow<List<LocalSubscriberMeterEntity>> = _meters.asStateFlow()
    private val _credits = MutableStateFlow<List<LocalSubscriberCreditEntity>>(emptyList())
    val credits: StateFlow<List<LocalSubscriberCreditEntity>> = _credits.asStateFlow()

    fun load(subscriberId: Int) {
        viewModelScope.launch {
            _subscriber.value = repository.getSubscriberById(subscriberId)
            _invoices.value = repository.getOpenInvoicesForSubscriber(subscriberId)
            _meters.value = repository.getMetersForSubscriber(subscriberId)
            _credits.value = repository.getCreditsForSubscriber(subscriberId)
        }
    }
}

class SubscriberDetailsViewModelFactory(
    private val repository: SubscriberRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SubscriberDetailsViewModel(repository) as T
    }
}
