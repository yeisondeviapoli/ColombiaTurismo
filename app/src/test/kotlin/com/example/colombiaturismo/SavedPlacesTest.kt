package com.example.colombiaturismo

import com.example.colombiaturismo.bogota.bogotaPlaces
import com.example.colombiaturismo.cartagena.cartagenaPlaces
import com.example.colombiaturismo.ibague.ibaguePlaces
import com.example.colombiaturismo.medellin.medellinPlaces
import com.example.colombiaturismo.pereira.pereiraPlaces
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SavedPlacesTest {

    private val allIds = bogotaPlaces.map { it.favoriteId } +
        cartagenaPlaces.map { it.favoriteId } +
        ibaguePlaces.map { it.favoriteId } +
        medellinPlaces.map { it.favoriteId } +
        pereiraPlaces.map { it.favoriteId }

    @Test
    fun `cada lugar tiene un id de favorito unico en toda la app`() {
        val repetidos = allIds.groupBy { it }.filterValues { it.size > 1 }.keys
        assertEquals("Ids repetidos: $repetidos", emptySet<String>(), repetidos)
    }

    @Test
    fun `el id empieza por la ciudad para abrir la pantalla de detalle correcta`() {
        val pantallas = setOf("bogota", "cartagena", "ibague", "medellin", "pereira")
        allIds.forEach { id -> assert(id.substringBefore('-') in pantallas) { "Id sin ciudad válida: $id" } }
    }

    @Test
    fun `todos los lugares se pueden mostrar en Guardados`() {
        allIds.forEach { id -> assertNotNull("Falta $id en el catálogo", placeCatalogById[id]) }
        assertEquals(allIds.size, placeCatalogById.size)
    }

    @Test
    fun `la busqueda encuentra lugares de cualquier ciudad y respeta la categoria`() {
        val catedral = placeCatalog.filter { it.matches("catedral", ALL_CATEGORIES) }.map { it.id }
        assert("pereira-catedral" in catedral)

        val porCiudad = placeCatalog.filter { it.matches("risaralda", ALL_CATEGORIES) }
        assertEquals(pereiraPlaces.size, porCiudad.size)

        val playas = placeCatalog.filter { it.matches("", PlaceCategory.PLAYAS.label) }
        assert(playas.isNotEmpty() && playas.all { PlaceCategory.PLAYAS in it.categories })
    }

    @Test
    fun `las cinco ciudades del documento tienen lugares`() {
        listOf(bogotaPlaces, cartagenaPlaces, ibaguePlaces, medellinPlaces, pereiraPlaces).forEach {
            assert(it.isNotEmpty())
        }
    }
}
