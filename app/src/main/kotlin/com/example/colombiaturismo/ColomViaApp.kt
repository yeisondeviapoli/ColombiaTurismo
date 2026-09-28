package com.example.colombiaturismo

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.colombiaturismo.data.AlmacenSesion
import com.example.colombiaturismo.data.ColomViaDatabase
import com.example.colombiaturismo.data.GestorFavoritos
import com.example.colombiaturismo.data.ServicioAutenticacion

class ColomViaApp : Application() {

    lateinit var contenedor: ContenedorApp
        private set

    override fun onCreate() {
        super.onCreate()
        contenedor = ContenedorApp(this)
    }
}

/** Dependencias compartidas por toda la app (una sola instancia de cada una). */
class ContenedorApp(context: Context) {

    private val baseDatos = ColomViaDatabase.crear(context)

    val servicioAutenticacion = ServicioAutenticacion(baseDatos.usuarioDao(), AlmacenSesion(context))

    val gestorFavoritos = GestorFavoritos(baseDatos.favoritoDao())
}

/** Acceso al contenedor desde las fábricas de ViewModel. */
val CreationExtras.contenedor: ContenedorApp
    get() = (checkNotNull(this[APPLICATION_KEY]) as ColomViaApp).contenedor
