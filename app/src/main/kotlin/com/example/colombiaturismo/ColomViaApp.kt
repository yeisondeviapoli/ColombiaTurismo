package com.example.colombiaturismo

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.colombiaturismo.data.AlmacenSesion
import com.example.colombiaturismo.data.ColomViaDatabase
import com.example.colombiaturismo.data.GestorFavoritos
import com.example.colombiaturismo.data.ServicioAutenticacion
import org.osmdroid.config.Configuration
import java.io.File

class ColomViaApp : Application() {

    lateinit var contenedor: ContenedorApp
        private set

    override fun onCreate() {
        super.onCreate()
        contenedor = ContenedorApp(this)
        configurarMapa()
    }

    /**
     * OpenStreetMap exige que la app se identifique con un User-Agent propio: con el nombre
     * de paquete genérico (com.example…) sus servidores devuelven mapas de "acceso bloqueado".
     * Los mapas descargados se guardan en la caché interna.
     */
    private fun configurarMapa() {
        val version = packageManager.getPackageInfo(packageName, 0).versionName
        Configuration.getInstance().apply {
            load(this@ColomViaApp, getSharedPreferences("osmdroid", MODE_PRIVATE))
            userAgentValue = "ColomVia/$version (Android; proyecto academico Politecnico Grancolombiano)"
            osmdroidBasePath = File(cacheDir, "osmdroid")
            osmdroidTileCache = File(cacheDir, "osmdroid/tiles")
        }
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
