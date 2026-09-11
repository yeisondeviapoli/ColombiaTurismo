package com.example.colombiaturismo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colombiaturismo.cartagena.CartagenaListScreen

private val Navy = Color(0xFF24455F)
private val NavyText = Color(0xFF1F3F5C)
private val Coral = Color(0xFFE8604C)
private val Muted = Color(0xFF7A8FA3)
private val Page = Color(0xFFF4F6F8)

data class Place(
    val name: String,
    val description: String,
    val category: String,
    val rating: String,
    val imageColor: Color
)

data class City(
    val name: String,
    val subtitle: String,
    val imageColor: Color,
    val icon: @Composable () -> Unit
)

private val cities = listOf(
    City("Bogotá", "Cultura y tradición", Color(0xFF85B7EB)) { Icon(Icons.Outlined.AccountBalance, null, tint = Color(0xFF042C53)) },
    City("Medellín", "Innovación y vida", Color(0xFFC0DD97)) { Icon(Icons.Outlined.Business, null, tint = Color(0xFF173404)) },
    City("Cartagena", "Historia y mar", Color(0xFFF5C4B3)) { Icon(Icons.Outlined.Museum, null, tint = Color(0xFF4A1B0C)) },
    City("Ibagué", "Música y naturaleza", Color(0xFFD8C6F0)) { Icon(Icons.Outlined.Park, null, tint = Color(0xFF43236A)) }
)

private val ibaguePlaces = listOf(
    Place("Cañón del Combeima", "Paisajes, montaña y naturaleza cerca de la ciudad.", "Naturaleza", "4.9", Color(0xFFAED8C0)),
    Place("Jardín Botánico San Jorge", "Un espacio natural para caminar y descubrir flora.", "Naturaleza", "4.8", Color(0xFFC5DEA2)),
    Place("Conservatorio del Tolima", "Música, historia y patrimonio cultural de Ibagué.", "Cultura", "4.7", Color(0xFFF4C77B)),
    Place("Plaza de Bolívar", "El corazón histórico y cultural de la ciudad.", "Historia", "4.7", Color(0xFFB9D4EC))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColombiaTurismoApp()
        }
    }
}

@Composable
fun ColombiaTurismoApp() {
    var screen by remember { mutableStateOf("home") }
    var savedCount by remember { mutableIntStateOf(1) }

    Surface(modifier = Modifier.fillMaxSize(), color = Page) {
        when (screen) {
            "ibague" -> CityScreen(
                onBack = { screen = "home" },
                savedCount = savedCount,
                onSave = { savedCount++ }
            )
            "cartagena" -> CartagenaListScreen()
            else -> HomeScreen(
                onIbagueClick = { screen = "ibague" },
                onCartagenaClick = { screen = "cartagena" },
                savedCount = savedCount
            )
        }
    }
}

@Composable
fun AppHeader(
    title: String,
    subtitle: String,
    onBack: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Navy)
            .padding(start = 16.dp, end = 16.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, "Volver", tint = Color.White)
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Coral),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Terrain, null, tint = Color.White)
                }
            }

            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Medium)
                Text(subtitle, color = Color(0xFFA8C0D2), fontSize = 11.sp)
            }

            Icon(Icons.Outlined.Share, "Compartir", tint = Color(0xFFC6D6E4), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Search, null, tint = Color(0xFF8AA0B2), modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(9.dp))
        Text("Buscar ciudad o lugar…", color = Color(0xFF8AA0B2), fontSize = 12.sp)
    }
}

@Composable
fun HomeScreen(onIbagueClick: () -> Unit, onCartagenaClick: () -> Unit, savedCount: Int) {
    Column(Modifier.fillMaxSize()) {
        AppHeader("Colombia Turismo", "Descubre la magia de nuestro país")
        Column(Modifier.fillMaxSize()) {
            Spacer(Modifier.height(13.dp))
            SearchBar()
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                FilterChip("Todos", true)
                FilterChip("Cultura", false)
                FilterChip("Playas", false)
                FilterChip("Naturaleza", false)
            }

            Row(Modifier.fillMaxSize()) {
                CityRail(onIbagueClick, onCartagenaClick)
                HomeFeatured(
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) Navy else Color.Transparent)
            .padding(horizontal = if (selected) 14.dp else 10.dp, vertical = 6.dp)
    ) {
        Text(text, color = if (selected) Color.White else Muted, fontSize = 11.sp)
    }
}

@Composable
fun CityRail(onIbagueClick: () -> Unit, onCartagenaClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(108.dp)
            .fillMaxHeight()
            .padding(start = 10.dp, end = 8.dp, bottom = 14.dp)
    ) {
        cities.forEach { city ->
            CityMiniCard(
                city = city,
                modifier = Modifier.clickable {
                    when (city.name) {
                        "Ibagué" -> onIbagueClick()
                        "Cartagena" -> onCartagenaClick()
                    }
                }
            )
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
fun CityMiniCard(city: City, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(city.imageColor),
            contentAlignment = Alignment.Center
        ) { city.icon() }
        Spacer(Modifier.height(7.dp))
        Text(city.name, color = NavyText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(city.subtitle, color = Color(0xFF6B8298), fontSize = 10.5.sp, lineHeight = 14.sp)
    }
}

@Composable
fun HomeFeatured(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(
                Color.White,
                RoundedCornerShape(topStart = 18.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(Modifier.weight(1f)) {
                Text(
                    "Bogotá",
                    color = NavyText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "Cultura y tradición",
                    color = Muted,
                    fontSize = 11.sp
                )
            }

            Text(
                "Ver mapa",
                color = Coral,
                fontSize = 11.sp
            )
        }

        Text(
            "Lugares destacados",
            color = NavyText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 15.dp, bottom = 9.dp)
        )

        FeaturedCard(
            "Cerro de Monserrate",
            "Vista increíble de la ciudad.",
            "4.9 · Naturaleza",
            Color(0xFFB5D4F4)
        )

        FeaturedCard(
            "Museo del Oro",
            "Arte ancestral de Colombia.",
            "4.8 · Historia",
            Color(0xFFFAC775)
        )
    }
}

@Composable
fun FeaturedCard(name: String, description: String, rating: String, color: Color) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 11.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(Color.White)
    ) {
        Box(Modifier.fillMaxWidth().height(66.dp).background(color), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Terrain, null, tint = NavyText, modifier = Modifier.size(25.dp))
        }
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(name, color = NavyText, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text(description, color = Muted, fontSize = 11.sp, lineHeight = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Star, null, tint = Color(0xFFC98A22), modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(rating, color = Color(0xFFC98A22), fontSize = 11.sp)
                }
            }
            Icon(Icons.Outlined.BookmarkBorder, null, tint = Color(0xFFA5B6C4), modifier = Modifier.size(17.dp))
        }
    }
}

@Composable
fun CityScreen(onBack: () -> Unit, savedCount: Int, onSave: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        AppHeader("Ibagué", "Música y naturaleza", onBack)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.Bottom) {
                    Column(Modifier.weight(1f)) {
                        Text("Ibagué", color = NavyText, fontSize = 23.sp, fontWeight = FontWeight.Medium)
                        Text("La ciudad musical de Colombia", color = Muted, fontSize = 12.sp)
                    }
                    Text("Ver mapa", color = Coral, fontSize = 11.sp)
                }
            }
            item {
                Box(
                    Modifier.fillMaxWidth().height(150.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFFD8C6F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Park, null, tint = Color(0xFF43236A), modifier = Modifier.size(55.dp))
                }
            }
            item {
                Text(
                    "Descubre Ibagué",
                    color = NavyText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    "Una ciudad rodeada de naturaleza y reconocida por su tradición musical. Explora sus lugares más representativos.",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
            item {
                Text("Lugares destacados", color = NavyText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            items(ibaguePlaces) { place ->
                PlaceCard(place, onSave)
            }
        }
    }
}

@Composable
fun PlaceCard(place: Place, onSave: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(Color.White)
    ) {
        Box(
            Modifier.fillMaxWidth().height(110.dp).background(place.imageColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Landscape, null, tint = NavyText, modifier = Modifier.size(38.dp))
        }
        Row(Modifier.padding(11.dp), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(place.name, color = NavyText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(place.description, color = Muted, fontSize = 11.sp, lineHeight = 16.sp, modifier = Modifier.padding(top = 3.dp, bottom = 5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Star, null, tint = Color(0xFFC98A22), modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${place.rating} · ${place.category}", color = Color(0xFFC98A22), fontSize = 11.sp)
                }
            }
            IconButton(onClick = onSave) {
                Icon(Icons.Outlined.BookmarkBorder, "Guardar", tint = Coral)
            }
        }
    }
}