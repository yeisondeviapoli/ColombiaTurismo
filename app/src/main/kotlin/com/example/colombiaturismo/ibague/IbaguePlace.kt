package com.example.colombiaturismo.ibague

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class IbaguePlace(
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
    val latitude: Double,   // Añadido para el Maps
    val longitude: Double   // Añadido para el Maps
)