package com.example.colombiaturismo.bogota

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
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

private val MutedText = Color(0xFF7A8FA3)
private val DarkText = Color(0xFF1F3F5C)

@Composable
fun BogotaListScreen(
    query: String = "",
    category: String = "Todos",
    onPlaceClick: (BogotaPlace) -> Unit = {}
) {
    val filteredPlaces = bogotaPlaces.filter { place ->
        val text = "${place.name} ${place.location} ${place.description}".lowercase()
        text.contains(query.trim().lowercase()) && (category == "Todos" ||
            when (category) {
                "Cultura" -> text.contains("museo") || text.contains("cultural") || text.contains("arte")
                "Historia" -> text.contains("histori") || text.contains("colonial") || text.contains("plaza")
                "Naturaleza" -> text.contains("montaña") || text.contains("sendero") || text.contains("sabana")
                else -> false
            })
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (filteredPlaces.isEmpty()) item { Text("No hay lugares que coincidan", color = MutedText, modifier = Modifier.padding(16.dp)) }
        items(filteredPlaces) { place ->
            BogotaCard(place = place, onClick = { onPlaceClick(place) })
        }
    }
}

@Composable
fun BogotaCard(
    place: BogotaPlace,
    onClick: () -> Unit
) {
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

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "Guardar",
                        tint = DarkText,
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