package com.example.colombiaturismo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** RF08: al escribir en el buscador del Home se buscan lugares de las cinco ciudades. */
@Composable
fun GlobalSearchResults(
    query: String,
    category: String,
    favoritesViewModel: FavoritesViewModel,
    onPlaceClick: (placeId: String) -> Unit
) {
    val results = remember(query, category) { placeCatalog.filter { it.matches(query, category) } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 12.dp, top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                pluralStringResource(R.plurals.busqueda_resultados, results.size, results.size),
                color = Color(0xFF7A8FA3),
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        if (results.isEmpty()) {
            item {
                Text(stringResource(R.string.mapa_sin_resultados), color = Color(0xFF7A8FA3), modifier = Modifier.padding(16.dp))
            }
        }
        items(results, key = { it.id }) { place ->
            CatalogPlaceCard(
                place = place,
                isSaved = favoritesViewModel.isFavorite(place.id),
                onClick = { onPlaceClick(place.id) },
                onToggleSaved = { favoritesViewModel.toggleFavorite(place.id) },
                imageHeight = 110.dp
            )
        }
    }
}
