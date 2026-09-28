package com.example.colombiaturismo.auth

import com.example.colombiaturismo.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun `nombre vacio o muy corto es invalido`() {
        assertEquals(R.string.error_nombre_vacio, AuthValidator.validateName("   "))
        assertEquals(R.string.error_nombre_corto, AuthValidator.validateName(" Al "))
        assertNull(AuthValidator.validateName("Ana María"))
    }

    @Test
    fun `correo debe tener formato valido`() {
        assertEquals(R.string.error_correo_vacio, AuthValidator.validateEmail(""))
        assertEquals(R.string.error_correo_invalido, AuthValidator.validateEmail("usuario@correo"))
        assertEquals(R.string.error_correo_invalido, AuthValidator.validateEmail("usuario.correo.com"))
        assertNull(AuthValidator.validateEmail("  usuario@poligran.edu.co "))
    }

    @Test
    fun `contrasena nueva exige 8 caracteres con letras y numeros`() {
        assertEquals(R.string.error_contrasena_vacia, AuthValidator.validateNewPassword(""))
        assertEquals(R.string.error_contrasena_debil, AuthValidator.validateNewPassword("abc123"))
        assertEquals(R.string.error_contrasena_debil, AuthValidator.validateNewPassword("abcdefgh"))
        assertEquals(R.string.error_contrasena_debil, AuthValidator.validateNewPassword("12345678"))
        assertNull(AuthValidator.validateNewPassword("colombia2026"))
    }

    @Test
    fun `login solo exige contrasena no vacia`() {
        assertEquals(R.string.error_contrasena_vacia, AuthValidator.validateLoginPassword(""))
        assertNull(AuthValidator.validateLoginPassword("x"))
    }

    @Test
    fun `confirmacion debe coincidir`() {
        assertEquals(R.string.error_confirmacion_vacia, AuthValidator.validateConfirmation("clave1234", ""))
        assertEquals(R.string.error_contrasenas_no_coinciden, AuthValidator.validateConfirmation("clave1234", "clave12345"))
        assertNull(AuthValidator.validateConfirmation("clave1234", "clave1234"))
    }
}
