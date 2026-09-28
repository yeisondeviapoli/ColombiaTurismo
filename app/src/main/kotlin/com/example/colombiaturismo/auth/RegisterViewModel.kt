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
import com.example.colombiaturismo.data.ResultadoRegistro
import com.example.colombiaturismo.data.ServicioAutenticacion
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmation: String = "",
    @StringRes val nameError: Int? = null,
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val confirmationError: Int? = null,
    val isLoading: Boolean = false
)

class RegisterViewModel(private val servicio: ServicioAutenticacion) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    init {
        viewModelScope.launch {
            servicio.usuarioActual.filterNotNull().collect { uiState = RegisterUiState() }
        }
    }

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value, nameError = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null, confirmationError = null)
    }

    fun onConfirmationChange(value: String) {
        uiState = uiState.copy(confirmation = value, confirmationError = null)
    }

    /** Al crear la cuenta se inicia sesión automáticamente (CU01). */
    fun register() {
        if (uiState.isLoading) return
        val state = uiState
        val validated = state.copy(
            nameError = AuthValidator.validateName(state.name),
            emailError = AuthValidator.validateEmail(state.email),
            passwordError = AuthValidator.validateNewPassword(state.password),
            confirmationError = AuthValidator.validateConfirmation(state.password, state.confirmation)
        )
        if (validated.hasErrors()) {
            uiState = validated
            return
        }

        uiState = validated.copy(isLoading = true)
        viewModelScope.launch {
            val resultado = servicio.registrarUsuario(state.name, state.email, state.password)
            if (resultado is ResultadoRegistro.CorreoEnUso) {
                uiState = uiState.copy(isLoading = false, emailError = R.string.error_correo_en_uso)
            }
        }
    }

    private fun RegisterUiState.hasErrors() =
        nameError != null || emailError != null || passwordError != null || confirmationError != null

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { RegisterViewModel(contenedor.servicioAutenticacion) }
        }
    }
}
