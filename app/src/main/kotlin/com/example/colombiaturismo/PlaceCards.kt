package com.example.colombiaturismo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkText = Color(0xFF1F3F5C)
private val MutedText = Color(0xFF7A8FA3)
private val SavedRed = Color(0xFFE53935)

/** Foto del lugar o, si no tiene, un ícono sobre su color de fondo. */
@Composable
fun PlaceThumbnail(place: CatalogPlace, modifier: Modifier = Modifier, iconSize: Dp = 40.dp) {
    Box(modifier.background(place.placeholderColor), contentAlignment = Alignment.Center) {
        if (place.imageRes != null) {
            Image(
                painter = painterResource(place.imageRes),
                contentDescription = place.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(Icons.Outlined.Place, contentDescription = null, tint = DarkText, modifier = Modifier.size(iconSize))
        }
    }
}

/** Tarjeta de un lugar de cualquier ciudad (Guardados y búsqueda en todas las ciudades). */
@Composable
fun CatalogPlaceCard(
    place: CatalogPlace,
    isSaved: Boolean,
    onClick: () -> Unit,
    onToggleSaved: () -> Unit,
    imageHeight: Dp = 130.dp
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(12.dp)) {
            Box {
                PlaceThumbnail(
                    place,
                    Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clip(RoundedCornerShape(12.dp))
                )
                IconButton(
                    onClick = onToggleSaved,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = stringResource(if (isSaved) R.string.cd_quitar_guardado else R.string.cd_guardar_lugar),
                        tint = if (isSaved) SavedRed else DarkText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(place.name, color = DarkText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = MutedText, modifier = Modifier.size(14.dp))
                        Text(
                            "${place.location}, ${place.city.name}",
                            color = MutedText,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFC98A22), modifier = Modifier.size(14.dp))
                        Text(" ${place.rating}", color = Color(0xFFC98A22), fontSize = 12.sp)
                    }
                }
                Icon(
                    Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.cd_ver_detalle),
                    tint = MutedText
                )
            }
        }
    }
}
