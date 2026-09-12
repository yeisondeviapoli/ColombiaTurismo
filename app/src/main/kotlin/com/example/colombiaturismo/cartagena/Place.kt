package com.example.colombiaturismo.cartagena

data class Place(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val rating: Double,
    val reviewsCount: Int,
    val schedule: String,
    val price: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val imageResId: Int,
    var isSaved: Boolean = false
)
