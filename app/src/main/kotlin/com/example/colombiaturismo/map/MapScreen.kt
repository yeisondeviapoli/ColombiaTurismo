package com.example.colombiaturismo.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.colombiaturismo.ALL_CATEGORIES
import com.example.colombiaturismo.CatalogPlace
import com.example.colombiaturismo.FavoritesViewModel
import com.example.colombiaturismo.PlaceThumbnail
import com.example.colombiaturismo.R
import com.example.colombiaturismo.RatingStars
import com.example.colombiaturismo.categoryFilters
import com.example.colombiaturismo.placeCatalog
import com.example.colombiaturismo.placeCatalogById
import com.example.colombiaturismo.ui.theme.ColomViaBlue
import com.example.colombiaturismo.ui.theme.ColomViaHint
import com.example.colombiaturismo.ui.theme.ColomViaNavy
import com.example.colombiaturismo.ui.theme.ColomViaText
import com.example.colombiaturismo.ui.theme.ColomViaTextMuted
import kotlinx.coroutines.launch
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourcePolicy
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

private val MarkerCoral = Color(0xFFE8604C)
private val ColombiaCenter = GeoPoint(5.6, -74.6)
private const val COUNTRY_ZOOM = 6.0
private const val PLACE_ZOOM = 15.0
private const val ANIMATION_MS = 800L

/**
 * Mapas de OpenStreetMap. Se define la fuente aquí en lugar de usar TileSourceFactory.MAPNIK
 * porque esa arma su propio User-Agent con el nombre del paquete (com.example…), que los
 * servidores de OSM bloquean; esta usa el User-Agent de la app (ver ColomViaApp) y mantiene
 * las reglas de uso de OSM: sin descargas masivas ni anticipadas.
 */
private val OpenStreetMapTiles = XYTileSource(
    "OpenStreetMap",
    0,
    19,
    256,
    ".png",
    arrayOf("https://tile.openstreetmap.org/"),
    "© colaboradores de OpenStreetMap",
    TileSourcePolicy(
        2,
        TileSourcePolicy.FLAG_NO_BULK or TileSourcePolicy.FLAG_NO_PREVENTIVE or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
    )
)

/**
 * Pantalla 4 del mockup / CU05: mapa interactivo con los lugares de las cinco ciudades.
 * Usa OpenStreetMap (osmdroid), que no necesita llave de API.
 */
@Composable
fun MapScreen(
    favoritesViewModel: FavoritesViewModel,
    selectedId: String?,
    onSelectedIdChange: (String?) -> Unit,
    onOpenDetail: (placeId: String) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf(ALL_CATEGORIES) }
    var showFilters by remember { mutableStateOf(false) }

    val visiblePlaces = remember(query, category) { placeCatalog.filter { it.matches(query, category) } }
    val selectedPlace = selectedId?.let { placeCatalogById[it] }

    val mapView = rememberMapViewWithLifecycle(
        initialFocus = selectedPlace?.let { GeoPoint(it.latitude, it.longitude) }
    )
    val myLocationOverlay = remember(mapView) {
        val provider = GpsMyLocationProvider(context).apply { addLocationSource(LocationManager.NETWORK_PROVIDER) }
        MyLocationNewOverlay(provider, mapView).apply {
            val dot = locationDot(context)
            setPersonIcon(dot)
            setDirectionIcon(dot)
            setPersonAnchor(0.5f, 0.5f)
            setDirectionAnchor(0.5f, 0.5f)
        }
    }
    DisposableEffect(myLocationOverlay) {
        onDispose { myLocationOverlay.disableMyLocation() }
    }

    val locationMessage = stringResource(R.string.mapa_buscando_ubicacion)
    val showMyLocation: () -> Unit = {
        if (!mapView.overlays.contains(myLocationOverlay)) mapView.overlays.add(myLocationOverlay)
        myLocationOverlay.enableMyLocation()
        val current = myLocationOverlay.myLocation
        if (current != null) {
            mapView.controller.animateTo(current, PLACE_ZOOM - 1, ANIMATION_MS)
        } else {
            scope.launch { snackbarHostState.showSnackbar(locationMessage) }
            myLocationOverlay.runOnFirstFix {
                mapView.post { myLocationOverlay.myLocation?.let { mapView.controller.animateTo(it, PLACE_ZOOM - 1, ANIMATION_MS) } }
            }
        }
    }
    val permissionMessage = stringResource(R.string.mapa_permiso_ubicacion)
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        if (results.values.any { it }) {
            showMyLocation()
        } else {
            scope.launch { snackbarHostState.showSnackbar(permissionMessage) }
        }
    }
    val onMyLocationClick: () -> Unit = {
        if (hasLocationPermission(context)) {
            showMyLocation()
        } else {
            // RNF07: el permiso se pide solo cuando el usuario quiere ver su ubicación.
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    // Al elegir un lugar (en el mapa o en la búsqueda) la cámara se acerca a él.
    LaunchedEffect(selectedId) {
        selectedPlace?.let { mapView.controller.animateTo(GeoPoint(it.latitude, it.longitude), PLACE_ZOOM, ANIMATION_MS) }
    }

    Box(Modifier.fillMaxSize()) {
        OsmMap(
            mapView = mapView,
            places = visiblePlaces,
            selectedId = selectedId,
            onMarkerClick = { id ->
                focusManager.clearFocus()
                onSelectedIdChange(id)
            },
            onMapClick = {
                focusManager.clearFocus()
                onSelectedIdChange(null)
            }
        )

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MapSearchField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {
                        focusManager.clearFocus()
                        visiblePlaces.firstOrNull()?.let { onSelectedIdChange(it.id) }
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(10.dp))
                Box {
                    val filterActive = category != ALL_CATEGORIES
                    IconButton(
                        onClick = { showFilters = true },
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(4.dp, CircleShape)
                            .background(if (filterActive) ColomViaBlue else Color.White, CircleShape)
                    ) {
                        Icon(
                            Icons.Outlined.Tune,
                            contentDescription = stringResource(R.string.mapa_filtrar),
                            tint = if (filterActive) Color.White else ColomViaNavy
                        )
                    }
                    DropdownMenu(
                        expanded = showFilters,
                        onDismissRequest = { showFilters = false },
                        containerColor = Color.White
                    ) {
                        categoryFilters.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = ColomViaText) },
                                trailingIcon = {
                                    if (option == category) Icon(Icons.Outlined.Check, contentDescription = null, tint = ColomViaBlue)
                                },
                                onClick = {
                                    category = option
                                    showFilters = false
                                    if (selectedPlace?.matches(query, option) == false) onSelectedIdChange(null)
                                    // Muestra en pantalla todos los lugares de la categoría elegida.
                                    mapView.fitPlaces(placeCatalog.filter { it.matches(query, option) })
                                }
                            )
                        }
                    }
                }
            }
            if (query.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                SearchSuggestions(
                    places = visiblePlaces.take(5),
                    onPlaceClick = { place ->
                        focusManager.clearFocus()
                        query = ""
                        onSelectedIdChange(place.id)
                    }
                )
            }
        }

        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    stringResource(R.string.mapa_atribucion),
                    color = ColomViaTextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = onMyLocationClick,
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(6.dp, CircleShape)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Outlined.MyLocation, contentDescription = stringResource(R.string.mapa_mi_ubicacion), tint = ColomViaNavy)
                }
            }
            if (selectedPlace != null) {
                Spacer(Modifier.height(14.dp))
                SelectedPlaceCard(
                    place = selectedPlace,
                    isSaved = favoritesViewModel.isFavorite(selectedPlace.id),
                    onToggleSaved = { favoritesViewModel.toggleFavorite(selectedPlace.id) },
                    onOpenDetail = { onOpenDetail(selectedPlace.id) }
                )
            }
        }

        SnackbarHost(snackbarHostState, Modifier.align(Alignment.Center))
    }
}

@Composable
private fun rememberMapViewWithLifecycle(initialFocus: GeoPoint?): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(OpenStreetMapTiles)
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            minZoomLevel = 4.0
            maxZoomLevel = 19.0
            if (initialFocus != null) {
                // Se vuelve de un detalle: el mapa sigue en el lugar elegido.
                controller.setZoom(PLACE_ZOOM)
                controller.setCenter(initialFocus)
            } else {
                controller.setZoom(COUNTRY_ZOOM)
                controller.setCenter(ColombiaCenter)
                // Al abrir, encuadra los lugares de las cinco ciudades (necesita conocer el tamaño de la vista).
                addOnFirstLayoutListener { _, _, _, _, _ -> fitPlaces(placeCatalog, animated = false) }
            }
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }
    return mapView
}

/** Dibuja un marcador por lugar; el seleccionado se resalta en coral, como en el mockup. */
@Composable
private fun OsmMap(
    mapView: MapView,
    places: List<CatalogPlace>,
    selectedId: String?,
    onMarkerClick: (String) -> Unit,
    onMapClick: () -> Unit
) {
    val context = LocalContext.current
    val navyPin = remember { tintedPin(context, ColomViaNavy) }
    val coralPin = remember { tintedPin(context, MarkerCoral) }
    val markers = remember { mutableListOf<Marker>() }
    val currentOnMapClick by rememberUpdatedState(onMapClick)
    val currentOnMarkerClick by rememberUpdatedState(onMarkerClick)

    DisposableEffect(mapView) {
        val tapOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                currentOnMapClick()
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean = false
        })
        // Debajo de los marcadores para que un toque sobre un marcador no cierre la tarjeta.
        mapView.overlays.add(0, tapOverlay)
        onDispose { mapView.overlays.remove(tapOverlay) }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
            view.overlays.removeAll(markers)
            markers.clear()
            // El seleccionado va al final para que se dibuje encima de los demás.
            places.sortedBy { it.id == selectedId }.forEach { place ->
                val selected = place.id == selectedId
                markers += Marker(view).apply {
                    position = GeoPoint(place.latitude, place.longitude)
                    title = place.name
                    icon = if (selected) coralPin else navyPin
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    setInfoWindow(null)
                    setOnMarkerClickListener { _, _ ->
                        currentOnMarkerClick(place.id)
                        true
                    }
                }
            }
            view.overlays.addAll(markers)
            view.invalidate()
        }
    )
}

private fun tintedPin(context: Context, color: Color): Drawable =
    ContextCompat.getDrawable(context, R.drawable.ic_map_pin)!!.mutate().apply { setTint(color.toArgb()) }

/** Encuadra los lugares dados; con uno solo, se acerca a él. */
private fun MapView.fitPlaces(places: List<CatalogPlace>, animated: Boolean = true) {
    when (places.size) {
        0 -> Unit
        1 -> controller.animateTo(GeoPoint(places[0].latitude, places[0].longitude), PLACE_ZOOM, ANIMATION_MS)
        else -> {
            val box = BoundingBox.fromGeoPoints(places.map { GeoPoint(it.latitude, it.longitude) })
            zoomToBoundingBox(box.increaseByScale(1.5f), animated)
        }
    }
}

/** Punto azul con borde blanco para la ubicación del usuario. */
private fun locationDot(context: Context): Bitmap {
    val density = context.resources.displayMetrics.density
    val size = (22 * density).toInt()
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val center = size / 2f
    paint.color = android.graphics.Color.WHITE
    canvas.drawCircle(center, center, center, paint)
    paint.color = ColomViaBlue.toArgb()
    canvas.drawCircle(center, center, center - 3 * density, paint)
    return bitmap
}

private fun hasLocationPermission(context: Context): Boolean =
    listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).any {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

@Composable
private fun MapSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(52.dp)
            .shadow(4.dp, RoundedCornerShape(26.dp))
            .background(Color.White, RoundedCornerShape(26.dp))
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Search, contentDescription = null, tint = ColomViaTextMuted, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = ColomViaText, fontSize = 15.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            modifier = Modifier.weight(1f),
            decorationBox = { field ->
                if (query.isEmpty()) Text(stringResource(R.string.mapa_buscar), color = ColomViaHint, fontSize = 15.sp)
                field()
            }
        )
        if (query.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }) {
                Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.cd_limpiar_busqueda), tint = ColomViaTextMuted)
            }
        }
    }
}

@Composable
private fun SearchSuggestions(places: List<CatalogPlace>, onPlaceClick: (CatalogPlace) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (places.isEmpty()) {
            Text(
                stringResource(R.string.mapa_sin_resultados),
                color = ColomViaTextMuted,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        places.forEach { place ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onPlaceClick(place) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlaceThumbnail(
                    place,
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    iconSize = 20.dp
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(place.name, color = ColomViaText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(place.city.region, color = ColomViaTextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}

/** Tarjeta inferior del mockup: foto, nombre, ciudad, calificación, guardar y "Ver detalle". */
@Composable
private fun SelectedPlaceCard(
    place: CatalogPlace,
    isSaved: Boolean,
    onToggleSaved: () -> Unit,
    onOpenDetail: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenDetail)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            PlaceThumbnail(
                place,
                Modifier
                    .size(width = 100.dp, height = 112.dp)
                    .clip(RoundedCornerShape(14.dp))
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        place.name,
                        color = ColomViaNavy,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onToggleSaved, modifier = Modifier.size(32.dp)) {
                        Icon(
                            if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = stringResource(if (isSaved) R.string.cd_quitar_guardado else R.string.cd_guardar_lugar),
                            tint = if (isSaved) Color(0xFFE53935) else ColomViaTextMuted
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = ColomViaTextMuted, modifier = Modifier.size(14.dp))
                    Text(place.city.region, color = ColomViaTextMuted, fontSize = 12.sp)
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingStars(place.rating, starSize = 14.dp)
                    Text("  ${place.rating}", color = ColomViaText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onOpenDetail,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColomViaNavy),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                ) {
                    Text(stringResource(R.string.mapa_ver_detalle), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
