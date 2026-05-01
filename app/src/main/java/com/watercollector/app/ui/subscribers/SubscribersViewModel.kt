package com.watercollector.app.ui.subscribers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.watercollector.app.data.local.entities.LocalSubscriberEntity
import com.watercollector.app.data.repository.SubscriberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SubscribersViewModel(
    repository: SubscriberRepository
) : ViewModel() {
    private val searchText = MutableStateFlow("")
    private val subscribers = repository.observeSubscribers()

    val items: StateFlow<List<LocalSubscriberEntity>> = combine(subscribers, searchText) { items, q ->
        val query = q.trim()
        if (query.isBlank()) items else items.filter {
            it.subscriberName.contains(query, true)
                    || (it.phoneNumber?.contains(query) == true)
                    || (it.primaryMeterNumber?.contains(query) == true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchText(value: String) { searchText.value = value }
}

class SubscribersViewModelFactory(
    private val repository: SubscriberRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SubscribersViewModel(repository) as T
    }
}
