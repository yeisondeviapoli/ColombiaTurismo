package com.example.colombiaturismo.bogota

import androidx.annotation.DrawableRes
import com.example.colombiaturismo.R

data class BogotaPlace(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val rating: Double,
    val schedule: String,
    val price: String,
    @DrawableRes val imageRes: Int
)

val bogotaPlaces = listOf(
    BogotaPlace(
        id = "1",
        name = "Cerro de Monserrate",
        location = "Cra. 2 Este #21-48, Paseo Bolívar",
        description = "A más de 3.100 metros sobre el nivel del mar, Monserrate es el mirador más emblemático de Bogotá. Se puede subir en teleférico, en funicular o a pie por un sendero de peregrinación. En la cima se encuentra el Santuario del Señor Caído, construido en el siglo XVII, junto a restaurantes tradicionales y una vista panorámica de toda la sabana que es especialmente espectacular al atardecer.",
        rating = 4.9,
        schedule = "Lun - Sáb 6:30 AM - 11:00 PM",
        price = "Gratis",
        imageRes = R.drawable.monserrate
    ),
    BogotaPlace(
        id = "2",
        name = "Museo del Oro",
        location = "Cra. 6 #15-88, Parque Santander",
        description = "Administrado por el Banco de la República, alberga la colección de orfebrería prehispánica más grande del mundo, con más de 34.000 piezas de oro y tumbaga. Sus salas recorren la historia, las creencias y la técnica de las culturas indígenas que habitaron el territorio colombiano, y culminan en la Sala de la Ofrenda, donde se evoca el ritual de la leyenda de El Dorado en la laguna de Guatavita.",
        rating = 4.8,
        schedule = "Mar - Sáb 9:00 AM - 7:00 PM",
        price = "$5.000 COP (domingos gratis)",
        imageRes = R.drawable.museo_oro
    ),
    BogotaPlace(
        id = "3",
        name = "La Candelaria",
        location = "Centro Histórico",
        description = "El barrio fundacional de Bogotá conserva calles empedradas, casas coloniales de colores y balcones de madera que narran más de cuatro siglos de historia. Es el epicentro cultural de la ciudad: aquí se concentran universidades, teatros, museos, arte urbano y cafés tradicionales donde probar un chocolate santafereño con queso o un tamal. Recorrerlo a pie es la mejor forma de descubrir su esencia bohemia.",
        rating = 4.7,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.la_candelaria
    ),
    BogotaPlace(
        id = "4",
        name = "Museo Botero",
        location = "Calle 11 #4-41, La Candelaria",
        description = "Ubicado en una hermosa casona colonial, reúne más de 120 obras donadas por el maestro Fernando Botero, con sus inconfundibles figuras voluminosas, además de piezas de su colección personal de artistas como Picasso, Monet, Renoir y Dalí. Sus patios interiores y corredores hacen de la visita una experiencia tranquila y enriquecedora en pleno centro histórico.",
        rating = 4.8,
        schedule = "Lun, Mié - Sáb 9:00 AM - 7:00 PM",
        price = "Gratis",
        imageRes = R.drawable.museo_botero
    ),
    BogotaPlace(
        id = "5",
        name = "Plaza de Bolívar",
        location = "Cra. 7 con Calle 10",
        description = "La plaza principal de Bogotá y el corazón político del país. Está rodeada por algunos de los edificios más importantes de Colombia: la Catedral Primada, el Capitolio Nacional, el Palacio de Justicia y el Palacio Liévano, sede de la Alcaldía. En su centro se alza la estatua de Simón Bolívar, y es escenario habitual de eventos culturales, manifestaciones y celebraciones nacionales.",
        rating = 4.6,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.plaza_bolivar
    )
)
