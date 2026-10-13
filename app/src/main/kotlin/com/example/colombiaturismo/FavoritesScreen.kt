package com.example.colombiaturismo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colombiaturismo.ui.theme.ColomViaBlue
import com.example.colombiaturismo.ui.theme.ColomViaHint
import com.example.colombiaturismo.ui.theme.ColomViaNavy
import com.example.colombiaturismo.ui.theme.ColomViaTextMuted
import com.example.colombiaturismo.ui.theme.ColomViaTopBar

private val MutedText = Color(0xFF7A8FA3)

/** Pantalla 6 del mockup: lugares guardados del usuario con sesión activa. */
@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel,
    onPlaceClick: (placeId: String) -> Unit,
    onExploreClick: () -> Unit,
    onBack: () -> Unit
) {
    val savedPlaces = favoritesViewModel.savedPlaceIds.mapNotNull { placeCatalogById[it] }

    FavoritesContent(
        savedPlaces = savedPlaces,
        onPlaceClick = onPlaceClick,
        onRemoveClick = favoritesViewModel::toggleFavorite,
        onExploreClick = onExploreClick,
        onBack = onBack
    )
}

@Composable
private fun FavoritesContent(
    savedPlaces: List<CatalogPlace>,
    onPlaceClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
    onExploreClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ColomViaTopBar(title = stringResource(R.string.guardados_titulo), onBack = onBack)

        if (savedPlaces.isEmpty()) {
            EmptySavedPlaces(onExploreClick)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        pluralStringResource(R.plurals.guardados_cantidad, savedPlaces.size, savedPlaces.size),
                        color = MutedText,
                        fontSize = 13.sp
                    )
                }
                items(savedPlaces, key = { it.id }) { place ->
                    CatalogPlaceCard(
                        place = place,
                        isSaved = true,
                        onClick = { onPlaceClick(place.id) },
                        onToggleSaved = { onRemoveClick(place.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptySavedPlaces(onExploreClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color(0xFFEEF1F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.BookmarkBorder, contentDescription = null, tint = ColomViaHint, modifier = Modifier.size(72.dp))
        }
        Spacer(Modifier.height(28.dp))
        Text(
            stringResource(R.string.guardados_vacio_titulo),
            color = ColomViaNavy,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )
        Spacer(Modifier.height(12.dp))
        Text(
            stringResource(R.string.guardados_vacio_texto),
            color = ColomViaTextMuted,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onExploreClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColomViaBlue),
            contentPadding = PaddingValues(horizontal = 36.dp),
            modifier = Modifier.height(52.dp)
        ) {
            Text(stringResource(R.string.guardados_explorar), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        // Compensa visualmente el encabezado para que el contenido quede centrado en la pantalla.
        Spacer(Modifier.height(60.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 760)
@Composable
private fun EmptyFavoritesPreview() {
    FavoritesContent(savedPlaces = emptyList(), onPlaceClick = {}, onRemoveClick = {}, onExploreClick = {}, onBack = {})
}
