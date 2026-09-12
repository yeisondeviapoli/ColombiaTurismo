package com.example.colombiaturismo.cartagena

import androidx.compose.ui.graphics.Color

// Definimos la clase aquí para que coincida con lo que piden las pantallas
data class CartagenaPlace(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val rating: Double,
    val schedule: String,
    val price: String,
    val imageColor: Color = Color(0xFFF5C4B3)
)

// Y de una vez dejamos la lista aquí guardada
val cartagenaPlaces = listOf(
    CartagenaPlace(
        id = "1",
        name = "Ciudad Amurallada",
        location = "Centro Histórico",
        description = "Centro histórico lleno de historia colonial y murallas imponentes.",
        rating = 4.9,
        schedule = "Abierto 24 horas",
        price = "Gratis"
    ),
    CartagenaPlace(
        id = "2",
        name = "Castillo de San Felipe",
        location = "Pie de la Popa",
        description = "Imponente fortaleza militar española construida en el siglo XVII.",
        rating = 4.8,
        schedule = "8:00 AM - 6:00 PM",
        price = "$30.000 COP"
    ),
    CartagenaPlace(
        id = "3",
        name = "Islas del Rosario",
        location = "Mar Caribe",
        description = "Archipiélago paradisíaco de aguas cristalinas y arrecifes de coral.",
        rating = 4.9,
        schedule = "Salidas 7:00 AM",
        price = "Varía según tour"
    )
)