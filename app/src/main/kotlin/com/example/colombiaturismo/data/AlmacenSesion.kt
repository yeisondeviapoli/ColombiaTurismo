package com.example.colombiaturismo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.sesionDataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion")

/**
 * Guarda la sesión activa para que el usuario no tenga que iniciar sesión
 * cada vez que abre la app. Solo se guarda el id del usuario, nunca la contraseña.
 */
class AlmacenSesion(context: Context) {

    private val dataStore = context.applicationContext.sesionDataStore

    val sesion: Flow<Sesion?> = dataStore.data
        .catch { error -> if (error is IOException) emit(emptyPreferences()) else throw error }
        .map { preferencias ->
            val idSesion = preferencias[ID_SESION]
            val idUsuario = preferencias[ID_USUARIO]
            val fechaInicio = preferencias[FECHA_INICIO]
            if (idSesion != null && idUsuario != null && fechaInicio != null) {
                Sesion(idSesion, idUsuario, fechaInicio)
            } else {
                null
            }
        }

    suspend fun guardar(sesion: Sesion) {
        dataStore.edit { preferencias ->
            preferencias[ID_SESION] = sesion.idSesion
            preferencias[ID_USUARIO] = sesion.idUsuario
            preferencias[FECHA_INICIO] = sesion.fechaInicio
        }
    }

    suspend fun limpiar() {
        dataStore.edit { it.clear() }
    }

    private companion object {
        val ID_SESION = stringPreferencesKey("id_sesion")
        val ID_USUARIO = stringPreferencesKey("id_usuario")
        val FECHA_INICIO = longPreferencesKey("fecha_inicio")
    }
}
