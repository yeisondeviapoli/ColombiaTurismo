package com.example.colombiaturismo.cartagena

import com.example.colombiaturismo.cityInfo
import com.example.colombiaturismo.CityHeader
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
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocationOn
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
import com.example.colombiaturismo.ALL_CATEGORIES
import com.example.colombiaturismo.FavoritesViewModel
import com.example.colombiaturismo.favoriteId
import com.example.colombiaturismo.matchesFilter

private val MutedText = Color(0xFF7A8FA3)
private val DarkText = Color(0xFF1F3F5C)

@Composable
fun CartagenaListScreen(
    query: String = "",
    category: String = ALL_CATEGORIES,
    favoritesViewModel: FavoritesViewModel, // <--- 1. Recibimos el ViewModel
    onPlaceClick: (CartagenaPlace) -> Unit = {}
) {
    val filteredPlaces = cartagenaPlaces.filter { place ->
        val text = "${place.name} ${place.location} ${place.description}".lowercase()
        text.contains(query.trim().lowercase()) && place.categories.matchesFilter(category)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { CityHeader(cityInfo("Cartagena")) }
        if (filteredPlaces.isEmpty()) {
            item {
                Text(
                    text = "No hay lugares que coincidan",
                    color = MutedText,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        items(filteredPlaces) { place ->
            // 2. Pasamos el ViewModel a cada tarjeta
            CartagenaCard(
                place = place,
                favoritesViewModel = favoritesViewModel,
                onClick = { onPlaceClick(place) }
            )
        }
    }
}

@Composable
fun CartagenaCard(
    place: CartagenaPlace,
    favoritesViewModel: FavoritesViewModel, // <--- Recibimos el ViewModel aquí
    onClick: () -> Unit
) {
    // 3. Verificamos si este lugar está marcado como favorito
    val isFav = favoritesViewModel.isFavorite(place.favoriteId)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = place.imageRes),
                    contentDescription = place.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // 4. Botón de favoritos interactivo
                IconButton(
                    onClick = { favoritesViewModel.toggleFavorite(place.favoriteId) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Guardar lugar",
                        tint = if (isFav) Color(0xFFE53935) else DarkText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = place.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = MutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = place.location,
                            fontSize = 12.sp,
                            color = MutedText
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