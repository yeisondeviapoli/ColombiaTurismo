package com.example.colombiaturismo.ibague

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
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

private val DarkText = Color(0xFF1F3F5C)
private val MutedText = Color(0xFF7A8FA3)

@Composable
fun IbagueListScreen(onPlaceClick: (IbaguePlace) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(end = 12.dp, top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(Modifier.padding(start = 4.dp, bottom = 2.dp)) {
                Text("Ibagué", color = DarkText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("La ciudad musical de Colombia", color = MutedText, fontSize = 12.sp)
            }
        }
        items(ibaguePlaces, key = { it.id }) { place ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth().clickable { onPlaceClick(place) }
            ) {
                Column(Modifier.padding(12.dp)) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(place.placeholderColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(place.imageRes),
                            contentDescription = place.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(place.name, color = DarkText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.LocationOn, null, tint = MutedText, modifier = Modifier.size(14.dp))
                                Text(place.location, color = MutedText, fontSize = 12.sp, maxLines = 1)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Star, null, tint = Color(0xFFC98A22), modifier = Modifier.size(14.dp))
                                Text(" ${place.rating}", color = Color(0xFFC98A22), fontSize = 12.sp)
                            }
                        }
                        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, "Ver detalle", tint = MutedText)
                    }
                }
            }
        }
    }
}
