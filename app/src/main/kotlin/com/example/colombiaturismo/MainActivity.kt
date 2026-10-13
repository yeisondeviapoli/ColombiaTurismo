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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colombiaturismo.auth.AuthFlow
import com.example.colombiaturismo.auth.SessionState
import com.example.colombiaturismo.auth.SessionViewModel
import com.example.colombiaturismo.bogota.BogotaDetailScreen
import com.example.colombiaturismo.bogota.BogotaListScreen
import com.example.colombiaturismo.bogota.BogotaPlace
import com.example.colombiaturismo.bogota.bogotaPlaces
import com.example.colombiaturismo.cartagena.CartagenaDetailScreen
import com.example.colombiaturismo.cartagena.CartagenaListScreen
import com.example.colombiaturismo.cartagena.CartagenaPlace
import com.example.colombiaturismo.cartagena.cartagenaPlaces
import com.example.colombiaturismo.ibague.IbagueDetailScreen
import com.example.colombiaturismo.ibague.IbagueListScreen
import com.example.colombiaturismo.map.MapScreen
import com.example.colombiaturismo.ibague.IbaguePlace
import com.example.colombiaturismo.ibague.ibaguePlaces
import com.example.colombiaturismo.medellin.MedellinDetailScreen
import com.example.colombiaturismo.medellin.MedellinListScreen
import com.example.colombiaturismo.medellin.MedellinPlace
import com.example.colombiaturismo.medellin.medellinPlaces
import com.example.colombiaturismo.pereira.PereiraDetailScreen
import com.example.colombiaturismo.pereira.PereiraListScreen
import com.example.colombiaturismo.pereira.PereiraPlace
import com.example.colombiaturismo.pereira.pereiraPlaces
import com.example.colombiaturismo.profile.LogoutConfirmDialog
import com.example.colombiaturismo.profile.ProfileRoute
import kotlinx.coroutines.launch

private val Navy = Color(0xFF24455F)
private val NavyText = Color(0xFF1F3F5C)
private val Muted = Color(0xFF7A8FA3)
private val Page = Color(0xFFF4F6F8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColomViaRoot()
        }
    }
}

/** Muestra Login/Registro si no hay sesión y la app cuando el usuario ya inició sesión. */
@Composable
fun ColomViaRoot(sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModel.Factory)) {
    val sessionState by sessionViewModel.sessionState.collectAsStateWithLifecycle()

    when (sessionState) {
        SessionState.Loading -> Box(Modifier.fillMaxSize().background(Color.White))
        SessionState.LoggedOut -> AuthFlow()
        is SessionState.LoggedIn -> ColombiaTurismoApp(onLogout = sessionViewModel::logout)
    }
}

@Composable
fun ColombiaTurismoApp(onLogout: () -> Unit) {
    // La navegación se guarda con rememberSaveable para no perderla al girar el teléfono.
    var screen by rememberSaveable { mutableStateOf("home") }
    // Lugar abierto en el detalle, por su id único (p. ej. "bogota-1").
    var selectedPlaceId by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedBogotaPlace = bogotaPlaces.find { it.favoriteId == selectedPlaceId }
    val selectedMedellinPlace = medellinPlaces.find { it.favoriteId == selectedPlaceId }
    val selectedCartagenaPlace = cartagenaPlaces.find { it.favoriteId == selectedPlaceId }
    val selectedIbaguePlace = ibaguePlaces.find { it.favoriteId == selectedPlaceId }
    val selectedPereiraPlace = pereiraPlaces.find { it.favoriteId == selectedPlaceId }
    // Pantalla a la que vuelve un detalle: el Home, el mapa o Guardados, según desde dónde se abrió.
    var detailReturnScreen by rememberSaveable { mutableStateOf("home") }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    // Estado del Home guardado aquí para que no se pierda al abrir un lugar y volver.
    var homeCity by rememberSaveable { mutableStateOf("Bogotá") }
    var homeQuery by rememberSaveable { mutableStateOf("") }
    var homeCategory by rememberSaveable { mutableStateOf(ALL_CATEGORIES) }
    var mapSelectedId by rememberSaveable { mutableStateOf<String?>(null) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Favoritos guardados en la base de datos del usuario con sesión activa
    val favoritesViewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory)
    val backFromDetail = { screen = detailReturnScreen }
    val openPlace = { placeId: String, from: String ->
        detailReturnScreen = from
        selectedPlaceId = placeId
        // El id empieza por la ciudad: "bogota-1" abre "bogota_detail".
        screen = placeId.substringBefore('-') + "_detail"
    }

    BackHandler(enabled = screen != "home") {
        screen = if (screen.endsWith("_detail")) detailReturnScreen else "home"
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
                    onMapClick = {
                        screen = "map"
                        scope.launch { drawerState.close() }
                    },
                    onFavoritesClick = {
                        screen = "favorites"
                        scope.launch { drawerState.close() }
                    },
                    onProfileClick = {
                        screen = "perfil"
                        scope.launch { drawerState.close() }
                    },
                    onLogoutClick = {
                        scope.launch { drawerState.close() }
                        showLogoutDialog = true
                    }
                )
            }
        ) {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f)) {
                    when (screen) {
                        "perfil" -> ProfileRoute(
                            onBack = { screen = "home" },
                            onLogoutClick = { showLogoutDialog = true }
                        )
                        "map" -> MapScreen(
                            favoritesViewModel = favoritesViewModel,
                            selectedId = mapSelectedId,
                            onSelectedIdChange = { mapSelectedId = it },
                            onOpenDetail = { placeId -> openPlace(placeId, "map") }
                        )
                        "favorites" -> FavoritesScreen(
                            favoritesViewModel = favoritesViewModel,
                            onPlaceClick = { placeId -> openPlace(placeId, "favorites") },
                            onExploreClick = { screen = "home" },
                            onBack = { screen = "home" }
                        )
                        "ibague_detail" -> selectedIbaguePlace?.let { place ->
                            IbagueDetailScreen(
                                place = place,
                                favoritesViewModel = favoritesViewModel,
                                onBack = backFromDetail
                            )
                        }
                        "medellin_detail" -> selectedMedellinPlace?.let { place ->
                            MedellinDetailScreen(
                                place = place,
                                favoritesViewModel = favoritesViewModel,
                                onBack = backFromDetail
                            )
                        }
                        "pereira_detail" -> selectedPereiraPlace?.let { place ->
                            PereiraDetailScreen(
                                place = place,
                                favoritesViewModel = favoritesViewModel,
                                onBack = backFromDetail
                            )
                        }
                        "cartagena_detail" -> {
                            val place = selectedCartagenaPlace
                            if (place != null) {
                                CartagenaDetailScreen(
                                    place = place,
                                    favoritesViewModel = favoritesViewModel,
                                    onBack = backFromDetail
                                )
                            }
                        }
                        "bogota_detail" -> {
                            val place = selectedBogotaPlace
                            if (place != null) {
                                BogotaDetailScreen(
                                    place = place,
                                    favoritesViewModel = favoritesViewModel,
                                    onBack = backFromDetail
                                )
                            }
                        }
                        else -> HomeScreen(
                            favoritesViewModel = favoritesViewModel,
                            selectedCity = homeCity,
                            onCitySelect = { homeCity = it },
                            query = homeQuery,
                            onQueryChange = { homeQuery = it },
                            category = homeCategory,
                            onCategoryChange = { homeCategory = it },
                            onOpenPlace = { placeId -> openPlace(placeId, "home") },
                            onMapClick = { screen = "map" },
                            onNavigateToIbagueDetail = { place -> openPlace(place.favoriteId, "home") },
                            onNavigateToMedellinDetail = { place -> openPlace(place.favoriteId, "home") },
                            onNavigateToDetail = { place -> openPlace(place.favoriteId, "home") },
                            onNavigateToBogotaDetail = { place -> openPlace(place.favoriteId, "home") },
                            onNavigateToPereiraDetail = { place -> openPlace(place.favoriteId, "home") },
                            onMenuClick = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                }
                AppBottomBar(
                    // Un detalle pertenece a la pestaña desde la que se abrió
                    currentScreen = if (screen.endsWith("_detail")) detailReturnScreen else screen,
                    onExploreClick = { screen = "home" },
                    onFavoritesClick = { screen = "favorites" },
                    onProfileClick = { screen = "perfil" }
                )
            }
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
fun AppBottomBar(
    currentScreen: String,
    onExploreClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Navy,
        selectedTextColor = Navy,
        indicatorColor = Color(0xFFE2E8F0),
        unselectedIconColor = Muted,
        unselectedTextColor = Muted
    )
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentScreen == "home" || currentScreen == "map",
            onClick = onExploreClick,
            icon = { Icon(Icons.Outlined.Explore, null) },
            colors = itemColors,
            label = { Text(stringResource(R.string.nav_explorar)) }
        )
        val savedSelected = currentScreen == "favorites"
        NavigationBarItem(
            selected = savedSelected,
            onClick = onFavoritesClick,
            icon = { Icon(if (savedSelected) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder, null) },
            colors = itemColors,
            label = { Text(stringResource(R.string.nav_guardados)) }
        )
        NavigationBarItem(
            selected = currentScreen == "perfil",
            onClick = onProfileClick,
            icon = { Icon(Icons.Outlined.Person, null) },
            colors = itemColors,
            label = { Text(stringResource(R.string.nav_perfil)) }
        )
    }
}

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val itemColors = NavigationDrawerItemDefaults.colors(
        unselectedContainerColor = Color.Transparent,
        unselectedIconColor = Color.White,
        unselectedTextColor = Color.White
    )
    ModalDrawerSheet(
        drawerState = drawerState,
        drawerContainerColor = Navy
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_colomvia_logo),
                    contentDescription = "Logo de Colom-Via",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "Colom-Via",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            NavigationDrawerItem(
                label = { Text("Home") },
                icon = { Icon(Icons.Outlined.Home, null) },
                selected = false,
                onClick = onHomeClick,
                colors = itemColors,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.mapa_titulo)) },
                icon = { Icon(Icons.Outlined.Map, null) },
                selected = false,
                onClick = onMapClick,
                colors = itemColors,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.nav_guardados)) },
                icon = { Icon(Icons.Outlined.BookmarkBorder, null) },
                selected = false,
                onClick = onFavoritesClick,
                colors = itemColors,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            NavigationDrawerItem(
                label = { Text("Perfil") },
                icon = { Icon(Icons.Outlined.Person, null) },
                selected = false,
                onClick = onProfileClick,
                colors = itemColors,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            icon = { Icon(Icons.AutoMirrored.Outlined.Logout, null) },
            selected = false,
            onClick = onLogoutClick,
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.White.copy(alpha = 0.12f),
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White
            ),
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 16.dp)
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
                Image(
                    painter = painterResource(R.drawable.ic_colomvia_logo),
                    contentDescription = "Logo de Colom-Via",
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Medium)
                Text(subtitle, color = Color(0xFFA8C0D2), fontSize = 11.sp)
            }

            val context = LocalContext.current
            IconButton(onClick = { shareApp(context) }) {
                Icon(Icons.Outlined.Share, stringResource(R.string.cd_compartir), tint = Color(0xFFC6D6E4), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
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
    favoritesViewModel: FavoritesViewModel,
    selectedCity: String,
    onCitySelect: (String) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    onOpenPlace: (placeId: String) -> Unit,
    onMapClick: () -> Unit,
    onNavigateToIbagueDetail: (IbaguePlace) -> Unit,
    onNavigateToMedellinDetail: (MedellinPlace) -> Unit,
    onNavigateToDetail: (CartagenaPlace) -> Unit,
    onNavigateToBogotaDetail: (BogotaPlace) -> Unit,
    onNavigateToPereiraDetail: (PereiraPlace) -> Unit,
    onMenuClick: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppHeader(title = "Colom-Via", subtitle = "¿A dónde quieres ir?", onMenuClick = onMenuClick)
        Spacer(Modifier.height(13.dp))
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(query = query, onQueryChange = onQueryChange, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = onMapClick,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Navy)
            ) {
                Icon(Icons.Outlined.Map, stringResource(R.string.cd_abrir_mapa), tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            categoryFilters.forEach { option ->
                FilterChip(text = option, selected = category == option, onClick = { onCategoryChange(option) })
            }
        }
        Row(Modifier.fillMaxSize()) {
            CityRail(
                selectedCity = selectedCity,
                onCitySelect = { city ->
                    onQueryChange("")
                    onCitySelect(city)
                }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                if (query.isNotBlank()) {
                    GlobalSearchResults(query, category, favoritesViewModel, onOpenPlace)
                } else when (selectedCity) {
                    "Cartagena" -> CartagenaListScreen(query, category, favoritesViewModel, onNavigateToDetail)
                    "Ibagué" -> IbagueListScreen(query, category, favoritesViewModel, onNavigateToIbagueDetail)
                    "Medellín" -> MedellinListScreen(query, category, favoritesViewModel, onNavigateToMedellinDetail)
                    "Pereira" -> PereiraListScreen(query, category, favoritesViewModel, onNavigateToPereiraDetail)
                    else -> BogotaListScreen(query, category, favoritesViewModel, onNavigateToBogotaDetail)
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
            .verticalScroll(rememberScrollState())
            .padding(start = 10.dp, end = 8.dp, bottom = 14.dp)
    ) {
        colombiaCities.forEach { city ->
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
    city: CityInfo,
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
            Image(
                painter = painterResource(id = city.imageRes),
                contentDescription = city.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.height(7.dp))
        Text(
            text = city.name,
            color = NavyText,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
        Text(city.nickname, color = Color(0xFF6B8298), fontSize = 10.5.sp, lineHeight = 14.sp)
    }
}
