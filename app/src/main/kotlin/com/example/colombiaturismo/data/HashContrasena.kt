package com.example.colombiaturismo.data

import android.os.Build
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Hash de contraseñas con PBKDF2 y sal aleatoria (RNF06).
 * El resultado incluye algoritmo, iteraciones y sal para poder verificarlo después:
 * `algoritmo$iteraciones$sal$hash`.
 *
 * Es una operación costosa a propósito: llamarla fuera del hilo principal.
 */
object HashContrasena {

    private const val ITERACIONES = 120_000
    private const val LONGITUD_CLAVE_BITS = 256
    private const val LONGITUD_SAL_BYTES = 16
    private const val SEPARADOR = "$"

    // PBKDF2WithHmacSHA256 solo está disponible desde Android 8.0 (API 26).
    private val algoritmo: String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) "PBKDF2WithHmacSHA256" else "PBKDF2WithHmacSHA1"

    private val aleatorio = SecureRandom()

    fun generar(contrasena: String): String {
        val sal = ByteArray(LONGITUD_SAL_BYTES).also { aleatorio.nextBytes(it) }
        val hash = derivar(contrasena, sal, algoritmo, ITERACIONES)
        return listOf(algoritmo, ITERACIONES.toString(), codificar(sal), codificar(hash))
            .joinToString(SEPARADOR)
    }

    fun verificar(contrasena: String, hashGuardado: String): Boolean {
        val partes = hashGuardado.split(SEPARADOR)
        if (partes.size != 4) return false
        val (algoritmoGuardado, iteraciones, sal, hash) = partes
        val calculado = derivar(contrasena, decodificar(sal), algoritmoGuardado, iteraciones.toInt())
        // Comparación en tiempo constante para no filtrar información por tiempos de respuesta.
        return MessageDigest.isEqual(calculado, decodificar(hash))
    }

    private fun derivar(contrasena: String, sal: ByteArray, algoritmo: String, iteraciones: Int): ByteArray {
        val especificacion = PBEKeySpec(contrasena.toCharArray(), sal, iteraciones, LONGITUD_CLAVE_BITS)
        return try {
            SecretKeyFactory.getInstance(algoritmo).generateSecret(especificacion).encoded
        } finally {
            especificacion.clearPassword()
        }
    }

    private fun codificar(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)

    private fun decodificar(texto: String): ByteArray = Base64.decode(texto, Base64.NO_WRAP)
}
