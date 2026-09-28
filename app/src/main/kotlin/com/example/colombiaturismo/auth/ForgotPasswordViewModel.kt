package com.example.colombiaturismo.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colombiaturismo.R
import com.example.colombiaturismo.contenedor
import com.example.colombiaturismo.data.ServicioAutenticacion
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val confirmation: String = "",
    @StringRes val emailError: Int? = null,
    @StringRes val nameError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val confirmationError: Int? = null,
    @StringRes val generalError: Int? = null,
    val isLoading: Boolean = false,
    /** Correo de la cuenta restablecida; la pantalla lo usa para volver al login. */
    val resetEmail: String? = null
)

class ForgotPasswordViewModel(private val servicio: ServicioAutenticacion) : ViewModel() {

    var uiState by mutableStateOf(ForgotPasswordUiState())
        private set

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null, generalError = null)
    }

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value, nameError = null, generalError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null, confirmationError = null)
    }

    fun onConfirmationChange(value: String) {
        uiState = uiState.copy(confirmation = value, confirmationError = null)
    }

    fun resetPassword() {
        if (uiState.isLoading) return
        val state = uiState
        val validated = state.copy(
            emailError = AuthValidator.validateEmail(state.email),
            nameError = AuthValidator.validateName(state.name),
            passwordError = AuthValidator.validateNewPassword(state.password),
            confirmationError = AuthValidator.validateConfirmation(state.password, state.confirmation),
            generalError = null
        )
        if (validated.emailError != null || validated.nameError != null ||
            validated.passwordError != null || validated.confirmationError != null
        ) {
            uiState = validated
            return
        }

        uiState = validated.copy(isLoading = true)
        viewModelScope.launch {
            val restablecida = servicio.restablecerContrasena(state.email, state.name, state.password)
            uiState = if (restablecida) {
                uiState.copy(isLoading = false, resetEmail = state.email.trim())
            } else {
                uiState.copy(isLoading = false, generalError = R.string.error_datos_recuperacion)
            }
        }
    }

    /** Limpia el formulario (incluidas las contraseñas) una vez se volvió al login. */
    fun onResetHandled() {
        uiState = ForgotPasswordUiState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ForgotPasswordViewModel(contenedor.servicioAutenticacion) }
        }
    }
}
