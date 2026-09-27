package com.example.colombiaturismo.medellin

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class MedellinPlace(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val rating: Double,
    val schedule: String,
    val price: String,
    @DrawableRes val imageRes: Int,
    val placeholderColor: Color,
    val icon: ImageVector
)
