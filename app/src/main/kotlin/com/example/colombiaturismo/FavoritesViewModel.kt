package com.example.colombiaturismo

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colombiaturismo.data.GestorFavoritos
import com.example.colombiaturismo.data.ServicioAutenticacion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Lugares guardados del usuario con sesión activa (CU04, RF09–RF11).
 * Se guardan en la base de datos por perfil: no se pierden al cerrar la app y
 * cada usuario ve solo los suyos. Los lugares se identifican con su `favoriteId`
 * (p. ej. "bogota-1"), ver SavedPlaces.kt.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel(
    servicio: ServicioAutenticacion,
    private val gestorFavoritos: GestorFavoritos
) : ViewModel() {

    private var idUsuario: String? = null

    /** Ids de los lugares guardados, del más reciente al más antiguo. */
    val savedPlaceIds = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            servicio.usuarioActual
                .map { it?.idUsuario }
                .distinctUntilChanged()
                .flatMapLatest { id ->
                    idUsuario = id
                    if (id == null) {
                        flowOf(emptyList())
                    } else {
                        gestorFavoritos.listarPorUsuario(id).map { favoritos -> favoritos.map { it.idLugar } }
                    }
                }
                .collect { ids ->
                    savedPlaceIds.clear()
                    savedPlaceIds.addAll(ids)
                }
        }
    }

    fun isFavorite(placeId: String): Boolean = placeId in savedPlaceIds

    fun toggleFavorite(placeId: String) {
        val usuario = idUsuario ?: return
        val guardar = placeId !in savedPlaceIds
        // Se actualiza la pantalla al instante; la base de datos confirma el cambio después.
        if (guardar) savedPlaceIds.add(0, placeId) else savedPlaceIds.remove(placeId)
        viewModelScope.launch {
            if (guardar) gestorFavoritos.agregar(usuario, placeId) else gestorFavoritos.eliminar(usuario, placeId)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { FavoritesViewModel(contenedor.servicioAutenticacion, contenedor.gestorFavoritos) }
        }
    }
}
