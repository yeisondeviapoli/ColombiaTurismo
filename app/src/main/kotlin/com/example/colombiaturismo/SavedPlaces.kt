package com.example.colombiaturismo

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.colombiaturismo.bogota.BogotaPlace
import com.example.colombiaturismo.bogota.bogotaPlaces
import com.example.colombiaturismo.cartagena.CartagenaPlace
import com.example.colombiaturismo.cartagena.cartagenaPlaces
import com.example.colombiaturismo.ibague.IbaguePlace
import com.example.colombiaturismo.ibague.ibaguePlaces
import com.example.colombiaturismo.medellin.MedellinPlace
import com.example.colombiaturismo.medellin.medellinPlaces
import com.example.colombiaturismo.pereira.PereiraPlace
import com.example.colombiaturismo.pereira.pereiraPlaces

// Identificador único de cada lugar en toda la app. Los ids de cada ciudad se repiten
// ("1", "2"…), por eso se antepone la ciudad. Es el idLugar que se guarda en favoritos.
val BogotaPlace.favoriteId: String get() = "bogota-$id"
val CartagenaPlace.favoriteId: String get() = "cartagena-$id"
val IbaguePlace.favoriteId: String get() = "ibague-$id"
val MedellinPlace.favoriteId: String get() = "medellin-$id"
val PereiraPlace.favoriteId: String get() = "pereira-$id"

/**
 * Resumen de un lugar de cualquier ciudad, con lo que necesitan las pantallas que muestran
 * lugares de varias ciudades a la vez: Guardados, el mapa y la búsqueda global.
 */
data class CatalogPlace(
    val id: String,
    val name: String,
    val location: String,
    val city: CityInfo,
    val rating: Double,
    @DrawableRes val imageRes: Int?,
    val placeholderColor: Color,
    val latitude: Double,
    val longitude: Double,
    val categories: Set<PlaceCategory>,
    val description: String
) {
    /** Texto que se busca con la barra de búsqueda. */
    val searchText: String by lazy { "$name $location ${city.name} ${city.department} $description".lowercase() }

    fun matches(query: String, category: String): Boolean =
        searchText.contains(query.trim().lowercase()) && categories.matchesFilter(category)
}

private val DefaultPlaceholder = Color(0xFFD6E4F0)

/** Todos los lugares de la app, en el orden de las ciudades del menú. */
val placeCatalog: List<CatalogPlace> by lazy {
    val bogota = cityInfo("Bogotá")
    val medellin = cityInfo("Medellín")
    val cartagena = cityInfo("Cartagena")
    val ibague = cityInfo("Ibagué")
    val pereira = cityInfo("Pereira")
    bogotaPlaces.map {
        CatalogPlace(it.favoriteId, it.name, it.location, bogota, it.rating, it.imageRes, it.placeholderColor, it.latitude, it.longitude, it.categories, it.description)
    } + medellinPlaces.map {
        CatalogPlace(it.favoriteId, it.name, it.location, medellin, it.rating, it.imageRes, it.placeholderColor, it.latitude, it.longitude, it.categories, it.description)
    } + cartagenaPlaces.map {
        CatalogPlace(it.favoriteId, it.name, it.location, cartagena, it.rating, it.imageRes, DefaultPlaceholder, it.latitude, it.longitude, it.categories, it.description)
    } + ibaguePlaces.map {
        CatalogPlace(it.favoriteId, it.name, it.location, ibague, it.rating, it.imageRes, it.placeholderColor, it.latitude, it.longitude, it.categories, it.description)
    } + pereiraPlaces.map {
        CatalogPlace(it.favoriteId, it.name, it.location, pereira, it.rating, it.imageRes, it.placeholderColor, it.latitude, it.longitude, it.categories, it.description)
    }
}

/** Lugares indexados por su [favoriteId]. */
val placeCatalogById: Map<String, CatalogPlace> by lazy { placeCatalog.associateBy { it.id } }
