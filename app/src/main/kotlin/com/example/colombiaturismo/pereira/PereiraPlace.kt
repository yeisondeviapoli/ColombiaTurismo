package com.example.colombiaturismo.pereira

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.colombiaturismo.PlaceCategory

data class PereiraPlace(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val rating: Double,
    val schedule: String,
    val price: String,
    /** Foto del lugar; si es `null` se muestra [placeholderColor] con [icon]. */
    @DrawableRes val imageRes: Int?,
    /** Autor y licencia de la foto (las de Wikimedia Commons exigen citarlos). */
    val imageCredit: String?,
    val placeholderColor: Color,
    val icon: ImageVector,
    val latitude: Double,
    val longitude: Double,
    val categories: Set<PlaceCategory>
)
