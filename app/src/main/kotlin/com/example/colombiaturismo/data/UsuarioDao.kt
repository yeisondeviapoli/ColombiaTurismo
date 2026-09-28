package com.example.colombiaturismo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE idUsuario = :idUsuario LIMIT 1")
    suspend fun buscarPorId(idUsuario: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE idUsuario = :idUsuario LIMIT 1")
    fun observarPorId(idUsuario: String): Flow<Usuario?>

    @Query("UPDATE usuarios SET nombre = :nombre WHERE idUsuario = :idUsuario")
    suspend fun actualizarNombre(idUsuario: String, nombre: String)

    @Query("UPDATE usuarios SET contrasenaHash = :contrasenaHash WHERE idUsuario = :idUsuario")
    suspend fun actualizarContrasena(idUsuario: String, contrasenaHash: String)
}
