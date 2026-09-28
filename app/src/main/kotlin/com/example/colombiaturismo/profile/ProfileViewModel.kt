package com.example.colombiaturismo.profile

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
import com.example.colombiaturismo.auth.AuthValidator
import com.example.colombiaturismo.contenedor
import com.example.colombiaturismo.data.GestorFavoritos
import com.example.colombiaturismo.data.ServicioAutenticacion
import com.example.colombiaturismo.data.Usuario
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val usuario: Usuario? = null,
    val savedPlacesCount: Int = 0
)

data class EditNameState(
    val name: String,
    @StringRes val error: Int? = null,
    val isSaving: Boolean = false
)

data class ChangePasswordState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmation: String = "",
    @StringRes val currentPasswordError: Int? = null,
    @StringRes val newPasswordError: Int? = null,
    @StringRes val confirmationError: Int? = null,
    val isSaving: Boolean = false
)

class ProfileViewModel(
    private val servicio: ServicioAutenticacion,
    gestorFavoritos: GestorFavoritos
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProfileUiState> = servicio.usuarioActual
        .flatMapLatest { usuario ->
            if (usuario == null) {
                flowOf(ProfileUiState())
            } else {
                gestorFavoritos.contarPorUsuario(usuario.idUsuario).map { ProfileUiState(usuario, it) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    /** Estado del diálogo "Editar nombre"; `null` si está cerrado. */
    var editNameState by mutableStateOf<EditNameState?>(null)
        private set

    /** Estado del diálogo "Cambiar contraseña"; `null` si está cerrado. */
    var changePasswordState by mutableStateOf<ChangePasswordState?>(null)
        private set

    /** Mensaje (recurso de texto) para mostrar en un Snackbar. */
    var message by mutableStateOf<Int?>(null)
        private set

    fun onEditNameClick() {
        editNameState = EditNameState(name = uiState.value.usuario?.nombre.orEmpty())
    }

    fun onNameChange(value: String) {
        editNameState = editNameState?.copy(name = value, error = null)
    }

    fun onEditNameDismiss() {
        if (editNameState?.isSaving != true) editNameState = null
    }

    fun saveName() {
        val state = editNameState ?: return
        if (state.isSaving) return
        val error = AuthValidator.validateName(state.name)
        if (error != null) {
            editNameState = state.copy(error = error)
            return
        }
        editNameState = state.copy(isSaving = true)
        viewModelScope.launch {
            servicio.actualizarNombre(state.name)
            editNameState = null
            message = R.string.perfil_nombre_actualizado
        }
    }

    fun onChangePasswordClick() {
        changePasswordState = ChangePasswordState()
    }

    fun onCurrentPasswordChange(value: String) {
        changePasswordState = changePasswordState?.copy(currentPassword = value, currentPasswordError = null)
    }

    fun onNewPasswordChange(value: String) {
        changePasswordState = changePasswordState?.copy(newPassword = value, newPasswordError = null, confirmationError = null)
    }

    fun onConfirmationChange(value: String) {
        changePasswordState = changePasswordState?.copy(confirmation = value, confirmationError = null)
    }

    fun onChangePasswordDismiss() {
        if (changePasswordState?.isSaving != true) changePasswordState = null
    }

    fun savePassword() {
        val state = changePasswordState ?: return
        if (state.isSaving) return
        val validated = state.copy(
            currentPasswordError = AuthValidator.validateLoginPassword(state.currentPassword),
            newPasswordError = AuthValidator.validateNewPassword(state.newPassword),
            confirmationError = AuthValidator.validateConfirmation(state.newPassword, state.confirmation)
        )
        if (validated.currentPasswordError != null || validated.newPasswordError != null ||
            validated.confirmationError != null
        ) {
            changePasswordState = validated
            return
        }
        changePasswordState = validated.copy(isSaving = true)
        viewModelScope.launch {
            val cambiada = servicio.cambiarContrasena(state.currentPassword, state.newPassword)
            if (cambiada) {
                changePasswordState = null
                message = R.string.perfil_contrasena_actualizada
            } else {
                changePasswordState = changePasswordState?.copy(
                    isSaving = false,
                    currentPasswordError = R.string.error_contrasena_actual
                )
            }
        }
    }

    fun onMessageShown() {
        message = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ProfileViewModel(contenedor.servicioAutenticacion, contenedor.gestorFavoritos) }
        }
    }
}
