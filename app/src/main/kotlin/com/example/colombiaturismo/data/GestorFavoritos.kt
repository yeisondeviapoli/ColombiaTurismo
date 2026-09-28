package com.example.colombiaturismo.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Favoritos por perfil (CU04). Todas las operaciones reciben el id del usuario, así cada
 * perfil ve solo sus lugares guardados y los conserva al cerrar e iniciar sesión.
 */
class GestorFavoritos(private val favoritoDao: FavoritoDao) {

    suspend fun agregar(idUsuario: String, idLugar: String): Favorito {
        val favorito = Favorito(
            idFavorito = UUID.randomUUID().toString(),
            idUsuario = idUsuario,
            idLugar = idLugar,
            fechaGuardado = System.currentTimeMillis()
        )
        favoritoDao.insertar(favorito)
        return favorito
    }

    suspend fun eliminar(idUsuario: String, idLugar: String): Boolean =
        favoritoDao.eliminar(idUsuario, idLugar) > 0

    fun listarPorUsuario(idUsuario: String): Flow<List<Favorito>> =
        favoritoDao.observarPorUsuario(idUsuario)

    fun estaGuardado(idUsuario: String, idLugar: String): Flow<Boolean> =
        favoritoDao.observarSiEstaGuardado(idUsuario, idLugar)

    fun contarPorUsuario(idUsuario: String): Flow<Int> =
        favoritoDao.observarCantidad(idUsuario)
}
