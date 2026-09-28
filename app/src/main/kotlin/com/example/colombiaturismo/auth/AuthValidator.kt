package com.example.colombiaturismo.auth

import androidx.annotation.StringRes
import com.example.colombiaturismo.R

/** Reglas de validación de los formularios de cuenta. Devuelven el mensaje de error o `null`. */
object AuthValidator {

    const val MIN_NAME_LENGTH = 3
    const val MIN_PASSWORD_LENGTH = 8

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    @StringRes
    fun validateName(name: String): Int? = when {
        name.isBlank() -> R.string.error_nombre_vacio
        name.trim().length < MIN_NAME_LENGTH -> R.string.error_nombre_corto
        else -> null
    }

    @StringRes
    fun validateEmail(email: String): Int? = when {
        email.isBlank() -> R.string.error_correo_vacio
        !EMAIL_REGEX.matches(email.trim()) -> R.string.error_correo_invalido
        else -> null
    }

    /** Para iniciar sesión solo se exige que no esté vacía. */
    @StringRes
    fun validateLoginPassword(password: String): Int? =
        if (password.isEmpty()) R.string.error_contrasena_vacia else null

    /** Contraseñas nuevas: mínimo 8 caracteres con al menos una letra y un número. */
    @StringRes
    fun validateNewPassword(password: String): Int? = when {
        password.isEmpty() -> R.string.error_contrasena_vacia
        password.length < MIN_PASSWORD_LENGTH ||
            password.none { it.isLetter() } ||
            password.none { it.isDigit() } -> R.string.error_contrasena_debil
        else -> null
    }

    @StringRes
    fun validateConfirmation(password: String, confirmation: String): Int? = when {
        confirmation.isEmpty() -> R.string.error_confirmacion_vacia
        confirmation != password -> R.string.error_contrasenas_no_coinciden
        else -> null
    }
}
