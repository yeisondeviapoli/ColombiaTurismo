package com.example.colombiaturismo

enum class PlaceCategory(val label: String) {
    CULTURA("Cultura"),
    NATURALEZA("Naturaleza"),
    HISTORIA("Historia"),
    PLAYAS("Playas")
}

const val ALL_CATEGORIES = "Todos"

val categoryFilters = listOf(ALL_CATEGORIES) + PlaceCategory.entries.map { it.label }

fun Set<PlaceCategory>.matchesFilter(filter: String): Boolean =
    filter == ALL_CATEGORIES || any { it.label == filter }
