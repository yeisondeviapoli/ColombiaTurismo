package com.example.colombiaturismo.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Usuario registrado en Colom-Via (módulo "Usuarios y sesión" del diagrama de clases).
 * El correo se guarda normalizado (sin espacios y en minúsculas) y es único.
 * Nunca se guarda la contraseña, solo su hash (ver [HashContrasena]).
 */
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class Usuario(
    @PrimaryKey val idUsuario: String,
    val nombre: String,
    val correo: String,
    val contrasenaHash: String,
    /** Fecha de registro en milisegundos (epoch). */
    val fechaRegistro: Long
)
