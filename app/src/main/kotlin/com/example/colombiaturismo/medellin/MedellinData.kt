package com.example.colombiaturismo.medellin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Train
import androidx.compose.ui.graphics.Color
import com.example.colombiaturismo.R

val medellinPlaces = listOf(
    MedellinPlace("pueblito-paisa", "Pueblito Paisa", "Cerro Nutibara, Medellín", "Uno de los lugares más representativos de Medellín, con arquitectura tradicional antioqueña y una vista panorámica de la ciudad.", 4.8, "Todos los días 5:00 AM - 11:00 PM", "Gratis", R.drawable.pueblitopaisa, Color(0xFFF2C27B), Icons.Outlined.AccountBalance),
    MedellinPlace("metrocable", "Metrocable de Medellín", "Sistema integrado de transporte", "Una experiencia única para contemplar las montañas y los barrios de Medellín mientras recorres la ciudad desde el aire.", 4.9, "Todos los días 4:30 AM - 11:00 PM", "Desde $3.500 COP", R.drawable.metrocable, Color(0xFFAED8C0), Icons.Outlined.Train),
    MedellinPlace("comuna-13", "Comuna 13", "San Javier, Medellín", "Un recorrido lleno de arte urbano, escaleras eléctricas, música y relatos de transformación social.", 4.9, "Todos los días 10:00 AM - 6:00 PM", "Gratis / tours opcionales", R.drawable.comuna13, Color(0xFFF3A68D), Icons.Outlined.Park),
    MedellinPlace("plaza-botero", "Plaza Botero", "Centro de Medellín", "Plaza pública que reúne esculturas del maestro Fernando Botero frente al Museo de Antioquia.", 4.7, "Todos los días 8:00 AM - 8:00 PM", "Gratis", R.drawable.plazabotero, Color(0xFFB9D4EC), Icons.Outlined.Museum),
    MedellinPlace("feria-flores", "Feria de las Flores", "Medellín y sus principales escenarios", "La celebración más tradicional de Medellín, famosa por los silleteros, los desfiles y la cultura antioqueña.", 4.8, "Temporada anual en agosto", "Según evento", R.drawable.feriaflores, Color(0xFFEAB8D6), Icons.Outlined.Park)
)
