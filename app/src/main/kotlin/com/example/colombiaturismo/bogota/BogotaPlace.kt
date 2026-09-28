package com.example.colombiaturismo.bogota

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class BogotaPlace(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val rating: Double,
    val schedule: String,
    val price: String,
    @DrawableRes val imageRes: Int,
    val placeholderColor: Color,
    val icon: ImageVector,
    val latitude: Double,   // Coordenada para Google Maps
    val longitude: Double   // Coordenada para Google Maps
)