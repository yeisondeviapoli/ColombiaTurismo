package com.example.colombiaturismo.cartagena

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkText = Color(0xFF1F3F5C)
private val MutedText = Color(0xFF7A8FA3)

@Composable
fun CartagenaDetailScreen(
    place: CartagenaPlace,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {
        // Barra superior con botón para volver
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkText)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = place.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Contenido principal del detalle
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8ECEF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Museum,
                    contentDescription = null,
                    tint = MutedText,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = place.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MutedText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = place.location,
                    color = MutedText,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = place.description,
                color = MutedText,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de información adicional (Horario, Precio y Rating)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Horario: ${place.schedule}", color = DarkText, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Precio: ${place.price}", color = DarkText, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Calificación: ${place.rating} ★", color = Color(0xFFC98A22), fontSize = 13.sp)
                }
            }
        }
    }
}