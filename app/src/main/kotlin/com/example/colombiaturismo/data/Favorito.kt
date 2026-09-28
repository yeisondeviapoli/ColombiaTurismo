package com.example.colombiaturismo.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Lugar guardado por un usuario (módulo "Favoritos" del diagrama de clases).
 * Cada favorito pertenece a un único perfil: al cerrar sesión no se pierde y
 * si se elimina el usuario se eliminan también sus favoritos.
 */
@Entity(
    tableName = "favoritos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["idUsuario", "idLugar"], unique = true)]
)
data class Favorito(
    @PrimaryKey val idFavorito: String,
    val idUsuario: String,
    /** Identificador global del lugar, p. ej. "cartagena-1". */
    val idLugar: String,
    /** Fecha en que se guardó, en milisegundos (epoch). */
    val fechaGuardado: Long
)
