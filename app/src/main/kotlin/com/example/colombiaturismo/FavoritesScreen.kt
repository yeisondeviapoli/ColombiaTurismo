package com.example.colombiaturismo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colombiaturismo.bogota.bogotaPlaces
import com.example.colombiaturismo.cartagena.cartagenaPlaces
import com.example.colombiaturismo.ibague.ibaguePlaces
import com.example.colombiaturismo.medellin.medellinPlaces

private val DarkText = Color(0xFF1F3F5C)
private val MutedText = Color(0xFF7A8FA3)

// Estructura adaptada con rating como String para evitar errores de tipo
data class UnifiedPlace(
    val name: String,
    val location: String,
    val rating: String,
    val imageRes: Int,
    val placeholderColor: Color
)

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel,
    onPlaceClick: (String) -> Unit
) {
    val favoriteNames = favoritesViewModel.favoritePlaces

    // Mapeamos los nombres buscando en los modelos de cada ciudad
    val favoritePlaces = favoriteNames.mapNotNull { name ->
        val bogota = bogotaPlaces.find { it.name == name }
        if (bogota != null) return@mapNotNull UnifiedPlace(
            name = bogota.name,
            location = bogota.location,
            rating = bogota.rating.toString(),
            imageRes = bogota.imageRes,
            placeholderColor = Color(0xFFD6E4F0)
        )

        val cartagena = cartagenaPlaces.find { it.name == name }
        if (cartagena != null) return@mapNotNull UnifiedPlace(
            name = cartagena.name,
            location = cartagena.location,
            rating = cartagena.rating.toString(),
            imageRes = cartagena.imageRes,
            placeholderColor = Color(0xFFD6E4F0)
        )

        val ibague = ibaguePlaces.find { it.name == name }
        if (ibague != null) return@mapNotNull UnifiedPlace(
            name = ibague.name,
            location = ibague.location,
            rating = ibague.rating.toString(),
            imageRes = ibague.imageRes,
            placeholderColor = Color(0xFFD6E4F0)
        )

        val medellin = medellinPlaces.find { it.name == name }
        if (medellin != null) return@mapNotNull UnifiedPlace(
            name = medellin.name,
            location = medellin.location,
            rating = medellin.rating.toString(),
            imageRes = medellin.imageRes,
            placeholderColor = medellin.placeholderColor
        )

        null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Lugares Favoritos",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = "Tus sitios guardados de todas las ciudades",
            fontSize = 12.sp,
            color = MutedText,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoritePlaces.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = MutedText,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Aún no tienes favoritos guardados",
                        color = MutedText,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoritePlaces, key = { it.name }) { place ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlaceClick(place.name) }
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(place.placeholderColor)
                            ) {
                                Image(
                                    painter = painterResource(place.imageRes),
                                    contentDescription = place.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                IconButton(
                                    onClick = { favoritesViewModel.toggleFavorite(place.name) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .size(32.dp)
                                        .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Bookmark,
                                        contentDescription = "Quitar Favorito",
                                        tint = Color(0xFFE53935),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = place.name,
                                        color = DarkText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Outlined.LocationOn,
                                            contentDescription = null,
                                            tint = MutedText,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = place.location,
                                            color = MutedText,
                                            fontSize = 12.sp,
                                            maxLines = 1
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Outlined.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFC98A22),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = " ${place.rating}",
                                            color = Color(0xFFC98A22),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                    contentDescription = "Ver detalle",
                                    tint = MutedText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}