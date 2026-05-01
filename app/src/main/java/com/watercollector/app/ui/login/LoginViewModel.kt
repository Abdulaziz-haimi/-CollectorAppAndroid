package com.watercollector.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.watercollector.app.data.repository.AuthRepository
import com.watercollector.app.data.repository.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun isLoggedIn() = sessionManager.isLoggedIn()
    fun getSavedBaseUrl() = sessionManager.baseUrl

    fun login(baseUrl: String, userName: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _message.value = null
            authRepository.login(baseUrl, userName, password)
                .onSuccess { onSuccess() }
                .onFailure { _message.value = it.message ?: "فشل تسجيل الدخول" }
            _loading.value = false
        }
    }
}

class LoginViewModelFactory(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authRepository, sessionManager) as T
    }
}
