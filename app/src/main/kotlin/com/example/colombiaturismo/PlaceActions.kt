package com.example.colombiaturismo

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * "Cómo llegar" (RF13): abre las indicaciones en Google Maps hacia el lugar.
 * Si Google Maps no está instalado, el enlace se abre en el navegador; si tampoco
 * hay navegador, se avisa al usuario en lugar de cerrar la app.
 */
fun openDirections(context: Context, latitude: Double, longitude: Double) {
    val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude")
    startSafely(context, Intent(Intent.ACTION_VIEW, uri), R.string.error_sin_app_mapas)
}

/** Comparte un lugar turístico con un enlace a su ubicación. */
fun sharePlace(context: Context, name: String, location: String, city: String, latitude: Double, longitude: Double) {
    val message = context.getString(
        R.string.compartir_lugar,
        name,
        city,
        location,
        "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
    )
    shareText(context, message)
}

/** Comparte la app desde el encabezado del Home. */
fun shareApp(context: Context) {
    shareText(context, context.getString(R.string.compartir_app))
}

private fun shareText(context: Context, text: String) {
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startSafely(context, Intent.createChooser(send, null), R.string.error_sin_app_compartir)
}

private fun startSafely(context: Context, intent: Intent, errorMessage: Int) {
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
