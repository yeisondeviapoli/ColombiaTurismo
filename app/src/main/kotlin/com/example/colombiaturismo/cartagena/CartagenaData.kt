package com.example.colombiaturismo.cartagena

import androidx.annotation.DrawableRes
import com.example.colombiaturismo.R

// Definimos la clase incluyendo el recurso de la imagen
data class CartagenaPlace(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val rating: Double,
    val schedule: String,
    val price: String,
    @DrawableRes val imageRes: Int // Referencia al recurso de imagen en drawable
)

// Lista con los 5 sitios principales de Cartagena enlazados a sus respectivas imágenes
val cartagenaPlaces = listOf(
    CartagenaPlace(
        id = "1",
        name = "Ciudad Amurallada",
        location = "Centro Histórico",
        description = "Centro histórico lleno de historia colonial y murallas imponentes.",
        rating = 4.9,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.ciudad_amurallada
    ),
    CartagenaPlace(
        id = "2",
        name = "Castillo de San Felipe",
        location = "Pie de la Popa",
        description = "Imponente fortaleza militar española construida en el siglo XVII.",
        rating = 4.8,
        schedule = "8:00 AM - 6:00 PM",
        price = "$30.000 COP",
        imageRes = R.drawable.san_felipe
    ),
    CartagenaPlace(
        id = "3",
        name = "Islas del Rosario",
        location = "Mar Caribe",
        description = "Archipiélago paradisíaco de aguas cristalinas y arrecifes de coral.",
        rating = 4.9,
        schedule = "Salidas 7:00 AM",
        price = "Varía según tour",
        imageRes = R.drawable.islas_rosario
    ),
    CartagenaPlace(
        id = "4",
        name = "Playa de Bocagrande",
        location = "Bocagrande",
        description = "Zona costera moderna con una amplia oferta hotelera, comercio y playa.",
        rating = 4.5,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.bocagrande
    ),
    CartagenaPlace(
        id = "5",
        name = "Convento de la Popa",
        location = "La Popa",
        description = "Monasterio ubicado en el punto más alto de la ciudad con vista panorámica.",
        rating = 4.7,
        schedule = "8:00 AM - 5:00 PM",
        price = "$20.000 COP",
        imageRes = R.drawable.convento_popa
    )
)