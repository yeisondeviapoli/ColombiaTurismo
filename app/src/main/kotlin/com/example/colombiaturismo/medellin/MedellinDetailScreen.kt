package com.example.colombiaturismo.medellin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MedellinDetailScreen(place: MedellinPlace, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFF0D233A)).verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().height(280.dp)) {
            Image(painterResource(place.imageRes), place.name, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            Row(Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.background(Color.Black.copy(alpha = .4f), CircleShape)) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Volver", tint = Color.White)
                }
                IconButton(onClick = {}, modifier = Modifier.background(Color.Black.copy(alpha = .4f), CircleShape)) {
                    Icon(Icons.Outlined.Share, "Compartir", tint = Color.White)
                }
            }
        }
        Surface(Modifier.fillMaxWidth().offset(y = (-22).dp), shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp), color = Color.White) {
            Column(Modifier.padding(24.dp)) {
                Text(place.name, color = Color(0xFF1F3F5C), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(place.location, color = Color(0xFF7A8FA3), fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
                Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { Icon(Icons.Outlined.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp)) }
                    Text("  ${place.rating}", color = Color(0xFF7A8FA3), fontSize = 13.sp)
                }
                Row(Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MedellinInfoCard("Horario", place.schedule, Icons.Outlined.Schedule, Modifier.weight(1f))
                    MedellinInfoCard("Precio", place.price, Icons.Outlined.ConfirmationNumber, Modifier.weight(1f))
                }
                Text("Sobre este lugar", color = Color(0xFF1F3F5C), fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
                Text(place.description, color = Color(0xFF7A8FA3), fontSize = 14.sp, lineHeight = 22.sp)
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
