package com.example.colombiaturismo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.example.colombiaturismo.bogota.BogotaDetailScreen
import com.example.colombiaturismo.bogota.BogotaListScreen
import com.example.colombiaturismo.bogota.BogotaPlace
import com.example.colombiaturismo.cartagena.CartagenaDetailScreen
import com.example.colombiaturismo.cartagena.CartagenaListScreen
import com.example.colombiaturismo.cartagena.CartagenaPlace
import com.example.colombiaturismo.ibague.IbagueDetailScreen
import com.example.colombiaturismo.ibague.IbagueListScreen
import com.example.colombiaturismo.ibague.IbaguePlace
import com.example.colombiaturismo.medellin.MedellinDetailScreen
import com.example.colombiaturismo.medellin.MedellinListScreen
import com.example.colombiaturismo.medellin.MedellinPlace
import kotlinx.coroutines.launch

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
        enableEdgeToEdge()
        setContent {
            ColombiaTurismoApp()
        }
    }
}

@Composable
fun ColombiaTurismoApp() {
    var screen by remember { mutableStateOf("home") }
    var selectedCartagenaPlace by remember { mutableStateOf<CartagenaPlace?>(null) }
    var selectedBogotaPlace by remember { mutableStateOf<BogotaPlace?>(null) }
    var selectedIbaguePlace by remember { mutableStateOf<IbaguePlace?>(null) }
    var selectedMedellinPlace by remember { mutableStateOf<MedellinPlace?>(null) }
    var savedCount by remember { mutableIntStateOf(1) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BackHandler(enabled = screen != "home") {
        screen = "home"
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        color = Page
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = screen == "home",
            drawerContent = {
                AppDrawer(
                    drawerState = drawerState,
                    onHomeClick = {
                        screen = "home"
                        scope.launch { drawerState.close() }
                    },
                    onLogoutClick = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            when (screen) {
                "ibague_detail" -> selectedIbaguePlace?.let { place ->
                    IbagueDetailScreen(place = place, onBack = { screen = "home" })
                }
                "medellin_detail" -> selectedMedellinPlace?.let { place ->
                    MedellinDetailScreen(place = place, onBack = { screen = "home" })
                }
                "cartagena_detail" -> {
                    val place = selectedCartagenaPlace
                    if (place != null) {
                        CartagenaDetailScreen(
                            place = place,
                            onBack = { screen = "home" }
                        )
                    }
                }
                "bogota_detail" -> {
                    val place = selectedBogotaPlace
                    if (place != null) {
                        BogotaDetailScreen(
                            place = place,
                            onBack = { screen = "home" }
                        )
                    }
                }
                else -> HomeScreen(
                    onNavigateToIbagueDetail = { place ->
                        selectedIbaguePlace = place
                        screen = "ibague_detail"
                    },
                    onNavigateToMedellinDetail = { place ->
                        selectedMedellinPlace = place
                        screen = "medellin_detail"
                    },
                    onNavigateToDetail = { place ->
                        selectedCartagenaPlace = place
                        screen = "cartagena_detail"
                    },
                    onNavigateToBogotaDetail = { place ->
                        selectedBogotaPlace = place
                        screen = "bogota_detail"
                    },
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    onHomeClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet(drawerState = drawerState) {
        Text(
            "Colom-Via",
            color = NavyText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )
        NavigationDrawerItem(
            label = { Text("Home") },
            icon = { Icon(Icons.Outlined.Home, null) },
            selected = false,
            onClick = onHomeClick,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            icon = { Icon(Icons.AutoMirrored.Outlined.Logout, null) },
            selected = false,
            onClick = onLogoutClick,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun AppHeader(
    title: String,
    subtitle: String,
    onBack: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Navy)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Volver", tint = Color.White)
                }
            } else {
                if (onMenuClick != null) {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Outlined.Menu, "Menú", tint = Color.White)
                    }
                }
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
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Search, null, tint = Color(0xFF8AA0B2), modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(9.dp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = NavyText, fontSize = 12.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
            decorationBox = { field ->
                if (query.isEmpty()) Text("Buscar ciudad o lugar…", color = Color(0xFF8AA0B2), fontSize = 12.sp)
                field()
            }
        )
    }
}

@Composable
fun HomeScreen(
    onNavigateToIbagueDetail: (IbaguePlace) -> Unit,
    onNavigateToMedellinDetail: (MedellinPlace) -> Unit,
    onNavigateToDetail: (CartagenaPlace) -> Unit,
    onNavigateToBogotaDetail: (BogotaPlace) -> Unit,
    onMenuClick: () -> Unit
) {
    var selectedCity by remember { mutableStateOf("Bogotá") }
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Todos") }

    Column(Modifier.fillMaxSize()) {
        AppHeader(title = "Colom-Via", subtitle = "¿A dónde quieres ir?", onMenuClick = onMenuClick)
        Spacer(Modifier.height(13.dp))
        SearchBar(query = query, onQueryChange = { query = it })
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            listOf("Todos", "Cultura", "Naturaleza", "Historia", "Playas").forEach { option ->
                FilterChip(text = option, selected = category == option, onClick = { category = option })
            }
        }
        Row(Modifier.fillMaxSize()) {
            CityRail(
                selectedCity = selectedCity,
                onCitySelect = { city ->
                    selectedCity = city
                }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                when (selectedCity) {
                    "Cartagena" -> CartagenaListScreen(query, category, onNavigateToDetail)
                    "Ibagué" -> IbagueListScreen(query, category, onNavigateToIbagueDetail)
                    "Medellín" -> MedellinListScreen(query, category, onNavigateToMedellinDetail)
                    else -> BogotaListScreen(query, category, onNavigateToBogotaDetail)
                }
            }
        }
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(50)).background(if (selected) Navy else Color.Transparent)
            .clickable(onClick = onClick).padding(horizontal = if (selected) 14.dp else 10.dp, vertical = 6.dp)
    ) { Text(text, color = if (selected) Color.White else Muted, fontSize = 11.sp) }
}

@Composable
fun CityRail(
    selectedCity: String,
    onCitySelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .width(108.dp)
            .fillMaxHeight()
            .padding(start = 10.dp, end = 8.dp, bottom = 14.dp)
    ) {
        cities.forEach { city ->
            val isSelected = city.name == selectedCity

            CityMiniCard(
                city = city,
                isSelected = isSelected,
                modifier = Modifier.clickable {
                    onCitySelect(city.name)
                }
            )
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
fun CityMiniCard(
    city: City,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFFE2E8F0) else Color.Transparent)
            .padding(8.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(city.imageColor),
            contentAlignment = Alignment.Center
        ) {
            // Cartagena
            if (city.name == "Cartagena") {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ciudad_amurallada),
                    contentDescription = city.name,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (city.name == "Bogotá") {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.bogota_ciudad),
                    contentDescription = city.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (city.name == "Ibagué") {
                Image(
                    painter = painterResource(id = R.drawable.combeima),
                    contentDescription = city.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (city.name == "Medellín") {
                Image(
                    painter = painterResource(id = R.drawable.pueblitopaisa),
                    contentDescription = city.name,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                city.icon()
            }
        }
        Spacer(Modifier.height(7.dp))
        Text(
            text = city.name,
            color = NavyText,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
        Text(city.subtitle, color = Color(0xFF6B8298), fontSize = 10.5.sp, lineHeight = 14.sp)
    }
}

// CORRECCIÓN 2: HomeFeatured ahora acepta datos dinámicos y scroll vertical
@Composable
fun HomeFeatured(
    cityName: String,
    subtitle: String,
    places: List<Place>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(
                Color.White,
                RoundedCornerShape(topStart = 18.dp)
            )
            .verticalScroll(rememberScrollState()) // Añadido scroll para evitar recortes
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(Modifier.weight(1f)) {
                Text(
                    cityName,
                    color = NavyText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    subtitle,
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

        places.forEach { place ->
            FeaturedCard(
                name = place.name,
                description = place.description,
                rating = "${place.rating} · ${place.category}",
                color = place.imageColor
            )
        }
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
fun CityScreen(onBack: () -> Unit, onSave: () -> Unit) {
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