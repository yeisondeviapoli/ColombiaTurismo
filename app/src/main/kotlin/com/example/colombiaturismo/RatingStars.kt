package com.example.colombiaturismo

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val StarYellow = Color(0xFFFFC107)

/** Cinco estrellas llenas, medias o vacías según la calificación (redondeada a 0,5). */
@Composable
fun RatingStars(rating: Double, modifier: Modifier = Modifier, starSize: Dp = 16.dp) {
    val halves = (rating.coerceIn(0.0, 5.0) * 2).roundToInt()
    Row(modifier.semantics { contentDescription = "Calificación $rating de 5" }) {
        repeat(5) { index ->
            val icon = when {
                halves >= (index + 1) * 2 -> Icons.Filled.Star
                halves == index * 2 + 1 -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarBorder
            }
            Icon(icon, contentDescription = null, tint = StarYellow, modifier = Modifier.size(starSize))
            if (index < 4) Spacer(Modifier.width(2.dp))
        }
    }
}
