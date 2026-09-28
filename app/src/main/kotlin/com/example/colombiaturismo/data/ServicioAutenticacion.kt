package com.example.colombiaturismo.data

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.UUID

sealed interface ResultadoRegistro {
    data class Exito(val usuario: Usuario) : ResultadoRegistro
    data object CorreoEnUso : ResultadoRegistro
}

/**
 * Registro, inicio y cierre de sesión (CU01) y gestión de la cuenta del usuario.
 * Los datos se guardan solo en el dispositivo.
 */
class ServicioAutenticacion(
    private val usuarioDao: UsuarioDao,
    private val almacenSesion: AlmacenSesion,
    private val despachadorHash: CoroutineDispatcher = Dispatchers.Default
) {

    /** Usuario con sesión activa, o `null` si no hay sesión. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val usuarioActual: Flow<Usuario?> = almacenSesion.sesion.flatMapLatest { sesion ->
        if (sesion == null) flowOf(null) else usuarioDao.observarPorId(sesion.idUsuario)
    }

    suspend fun registrarUsuario(nombre: String, correo: String, contrasena: String): ResultadoRegistro {
        val correoNormalizado = normalizarCorreo(correo)
        if (usuarioDao.buscarPorCorreo(correoNormalizado) != null) return ResultadoRegistro.CorreoEnUso

        val usuario = Usuario(
            idUsuario = UUID.randomUUID().toString(),
            nombre = normalizarNombre(nombre),
            correo = correoNormalizado,
            contrasenaHash = withContext(despachadorHash) { HashContrasena.generar(contrasena) },
            fechaRegistro = System.currentTimeMillis()
        )
        try {
            usuarioDao.insertar(usuario)
        } catch (e: SQLiteConstraintException) {
            // Otro registro con el mismo correo se guardó mientras se calculaba el hash.
            return ResultadoRegistro.CorreoEnUso
        }
        abrirSesion(usuario)
        return ResultadoRegistro.Exito(usuario)
    }

    /** Devuelve la sesión creada, o `null` si el correo o la contraseña no son correctos. */
    suspend fun iniciarSesion(correo: String, contrasena: String): Sesion? {
        val usuario = buscarConCredenciales(correo, contrasena) ?: return null
        return abrirSesion(usuario)
    }

    suspend fun cerrarSesion() {
        almacenSesion.limpiar()
    }

    suspend fun validarCredenciales(correo: String, contrasena: String): Boolean =
        buscarConCredenciales(correo, contrasena) != null

    suspend fun actualizarNombre(nombre: String) {
        val idUsuario = idUsuarioActual() ?: return
        usuarioDao.actualizarNombre(idUsuario, normalizarNombre(nombre))
    }

    /** Devuelve `false` si la contraseña actual no es correcta. */
    suspend fun cambiarContrasena(contrasenaActual: String, contrasenaNueva: String): Boolean {
        val idUsuario = idUsuarioActual() ?: return false
        val usuario = usuarioDao.buscarPorId(idUsuario) ?: return false
        val actualCorrecta = withContext(despachadorHash) {
            HashContrasena.verificar(contrasenaActual, usuario.contrasenaHash)
        }
        if (!actualCorrecta) return false
        guardarContrasena(usuario.idUsuario, contrasenaNueva)
        return true
    }

    /**
     * Recuperación local de la contraseña: como la app no tiene servidor para enviar
     * correos, el usuario confirma su identidad con el correo y el nombre con los que
     * se registró. Devuelve `false` si los datos no coinciden con ninguna cuenta.
     */
    suspend fun restablecerContrasena(correo: String, nombre: String, contrasenaNueva: String): Boolean {
        val usuario = usuarioDao.buscarPorCorreo(normalizarCorreo(correo)) ?: return false
        if (compararNombre(usuario.nombre) != compararNombre(nombre)) return false
        guardarContrasena(usuario.idUsuario, contrasenaNueva)
        return true
    }

    private suspend fun buscarConCredenciales(correo: String, contrasena: String): Usuario? {
        val usuario = usuarioDao.buscarPorCorreo(normalizarCorreo(correo)) ?: return null
        val valida = withContext(despachadorHash) { HashContrasena.verificar(contrasena, usuario.contrasenaHash) }
        return if (valida) usuario else null
    }

    private suspend fun guardarContrasena(idUsuario: String, contrasena: String) {
        val hash = withContext(despachadorHash) { HashContrasena.generar(contrasena) }
        usuarioDao.actualizarContrasena(idUsuario, hash)
    }

    private suspend fun abrirSesion(usuario: Usuario): Sesion {
        val sesion = Sesion(
            idSesion = UUID.randomUUID().toString(),
            idUsuario = usuario.idUsuario,
            fechaInicio = System.currentTimeMillis()
        )
        almacenSesion.guardar(sesion)
        return sesion
    }

    private suspend fun idUsuarioActual(): String? = almacenSesion.sesion.first()?.idUsuario

    private fun normalizarCorreo(correo: String) = correo.trim().lowercase(Locale.ROOT)

    private fun normalizarNombre(nombre: String) = nombre.trim().replace(ESPACIOS, " ")

    private fun compararNombre(nombre: String) = normalizarNombre(nombre).lowercase(Locale.ROOT)

    private companion object {
        val ESPACIOS = Regex("\\s+")
    }
}
