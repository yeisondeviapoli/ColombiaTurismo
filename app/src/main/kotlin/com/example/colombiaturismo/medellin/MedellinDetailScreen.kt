package com.example.colombiaturismo.medellin

import com.example.colombiaturismo.RatingStars
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colombiaturismo.FavoritesViewModel
import com.example.colombiaturismo.favoriteId
import com.example.colombiaturismo.openDirections
import com.example.colombiaturismo.sharePlace

@Composable
fun MedellinDetailScreen(
    place: MedellinPlace,
    favoritesViewModel: FavoritesViewModel, // <--- 1. Recibimos el ViewModel
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // 2. Consultamos si el lugar de Medellín es favorito
    val isFav = favoritesViewModel.isFavorite(place.favoriteId)

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Box(Modifier.fillMaxWidth().height(280.dp)) {
            Image(
                painter = painterResource(place.imageRes),
                contentDescription = place.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.background(Color.Black.copy(alpha = .4f), CircleShape)) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Volver", tint = Color.White)
                }
                IconButton(
                    onClick = { sharePlace(context, place.name, place.location, "Medellín", place.latitude, place.longitude) },
                    modifier = Modifier.background(Color.Black.copy(alpha = .4f), CircleShape)
                ) {
                    Icon(Icons.Outlined.Share, "Compartir", tint = Color.White)
                }
            }
        }
        Surface(
            Modifier
                .fillMaxWidth()
                .offset(y = (-22).dp),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Color.White
        ) {
            Column(Modifier.padding(24.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        place.name,
                        color = Color(0xFF1F3F5C),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    // 3. Botón de favoritos conectado al ViewModel
                    IconButton(onClick = { favoritesViewModel.toggleFavorite(place.favoriteId) }) {
                        Icon(
                            imageVector = if (isFav) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Guardar lugar",
                            tint = Color(0xFFE53935)
                        )
                    }
                }
                Text(place.location, color = Color(0xFF7A8FA3), fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
                Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    RatingStars(place.rating)
                    Text("  ${place.rating}", color = Color(0xFF7A8FA3), fontSize = 13.sp)
                }
                Row(Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MedellinInfoCard("Horario", place.schedule, Icons.Outlined.Schedule, Modifier.weight(1f))
                    MedellinInfoCard("Precio", place.price, Icons.Outlined.ConfirmationNumber, Modifier.weight(1f))
                }
                Text("Sobre este lugar", color = Color(0xFF1F3F5C), fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
                Text(place.description, color = Color(0xFF7A8FA3), fontSize = 14.sp, lineHeight = 22.sp)

                Spacer(Modifier.height(20.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FA)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(70.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFD6E4F0)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.LocationOn, null, tint = Color(0xFFE53935), modifier = Modifier.size(32.dp))
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(place.location, color = Color(0xFF1F3F5C), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Medellín, Antioquia, Colombia", color = Color(0xFF7A8FA3), fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { openDirections(context, place.latitude, place.longitude) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D233A)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Icon(Icons.Outlined.Navigation, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Cómo llegar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MedellinInfoCard(title: String, value: String, icon: ImageVector, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FA)), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = Color(0xFF1F3F5C), modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(5.dp))
                Text(title, color = Color(0xFF1F3F5C), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Text(value, color = Color(0xFF7A8FA3), fontSize = 11.sp, lineHeight = 15.sp, modifier = Modifier.padding(top = 6.dp))
        }
    }
}