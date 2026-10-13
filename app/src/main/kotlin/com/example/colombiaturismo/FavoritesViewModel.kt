package com.example.colombiaturismo

import androidx.compose.runtime.mutableStateSetOf
import androidx.lifecycle.ViewModel

class FavoritesViewModel : ViewModel() {
    // Almacena los nombres o identificadores de los lugares favoritos
    val favoritePlaces = mutableStateSetOf<String>()

    // Agrega o elimina un lugar de los favoritos
    fun toggleFavorite(placeName: String) {
        if (favoritePlaces.contains(placeName)) {
            favoritePlaces.remove(placeName)
        } else {
            favoritePlaces.add(placeName)
        }
    }

    // Verifica si un lugar específico es favorito
    fun isFavorite(placeName: String): Boolean {
        return favoritePlaces.contains(placeName)
    }
}