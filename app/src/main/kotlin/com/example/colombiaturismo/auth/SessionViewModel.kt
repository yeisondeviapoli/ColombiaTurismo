package com.example.colombiaturismo.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colombiaturismo.contenedor
import com.example.colombiaturismo.data.ServicioAutenticacion
import com.example.colombiaturismo.data.Usuario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SessionState {
    /** Leyendo la sesión guardada; se evita mostrar el login por un instante. */
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data class LoggedIn(val usuario: Usuario) : SessionState
}

/** Decide si se muestra el flujo de cuenta o la app, y cierra la sesión. */
class SessionViewModel(private val servicio: ServicioAutenticacion) : ViewModel() {

    val sessionState: StateFlow<SessionState> = servicio.usuarioActual
        .map { usuario -> if (usuario == null) SessionState.LoggedOut else SessionState.LoggedIn(usuario) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SessionState.Loading)

    fun logout() {
        viewModelScope.launch { servicio.cerrarSesion() }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { SessionViewModel(contenedor.servicioAutenticacion) }
        }
    }
}
