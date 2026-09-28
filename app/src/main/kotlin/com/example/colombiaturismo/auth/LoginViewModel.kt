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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val generalError: Int? = null,
    @StringRes val infoMessage: Int? = null,
    val isLoading: Boolean = false
)

class LoginViewModel(private val servicio: ServicioAutenticacion) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    init {
        // Al iniciar una sesión (desde login o registro) el formulario queda limpio para el próximo ingreso.
        viewModelScope.launch {
            servicio.usuarioActual.filterNotNull().collect { uiState = LoginUiState() }
        }
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null, generalError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null, generalError = null)
    }

    fun login() {
        if (uiState.isLoading) return
        val email = uiState.email
        val password = uiState.password
        val emailError = AuthValidator.validateEmail(email)
        val passwordError = AuthValidator.validateLoginPassword(password)
        if (emailError != null || passwordError != null) {
            uiState = uiState.copy(emailError = emailError, passwordError = passwordError)
            return
        }

        uiState = uiState.copy(isLoading = true, generalError = null)
        viewModelScope.launch {
            val sesion = servicio.iniciarSesion(email, password)
            if (sesion == null) {
                uiState = uiState.copy(isLoading = false, generalError = R.string.error_credenciales)
            }
        }
    }

    /** Tras restablecer la contraseña se vuelve al login con el correo ya escrito. */
    fun onPasswordReset(email: String) {
        uiState = LoginUiState(email = email, infoMessage = R.string.info_contrasena_restablecida)
    }

    fun onInfoMessageShown() {
        uiState = uiState.copy(infoMessage = null)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { LoginViewModel(contenedor.servicioAutenticacion) }
        }
    }
}
