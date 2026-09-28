package com.example.colombiaturismo.data

/** Sesión iniciada en el dispositivo. Un usuario mantiene como máximo una sesión activa. */
data class Sesion(
    val idSesion: String,
    val idUsuario: String,
    /** Momento de inicio de sesión en milisegundos (epoch). */
    val fechaInicio: Long,
    val activa: Boolean = true
) {
    fun estaActiva(): Boolean = activa
}
