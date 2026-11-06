package com.example.tokokomputer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tokokomputer.domain.LoginValidator
import com.example.tokokomputer.model.LoggedInUser
import com.example.tokokomputer.model.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val isLoggedIn: Boolean = false,
    val user: LoggedInUser? = null,
    val errorMessage: String? = null
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(
        usernameOrEmail: String,
        password: String
    ) {
        when (
            val result = LoginValidator.login(
                usernameOrEmail = usernameOrEmail,
                password = password
            )
        ) {
            is LoginResult.Success -> {
                _uiState.value = LoginUiState(
                    isLoggedIn = true,
                    user = result.user,
                    errorMessage = null
                )
            }

            is LoginResult.Error -> {
                _uiState.value = LoginUiState(
                    isLoggedIn = false,
                    user = null,
                    errorMessage = result.message
                )
            }
        }
    }

    fun logout() {
        _uiState.value = LoginUiState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}