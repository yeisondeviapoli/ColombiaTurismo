package com.example.colombiaturismo.ibague

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Place
import androidx.compose.ui.graphics.Color
import com.example.colombiaturismo.R

val ibaguePlaces = listOf(
    IbaguePlace(
        "combeima",
        "Cañón del Combeima",
        "Vía al Cañón del Combeima",
        "Un corredor natural de montañas, ríos y miradores a pocos minutos de Ibagué, ideal para caminar y disfrutar de la biodiversidad andina.",
        4.9,
        "Todos los días 6:00 AM - 6:00 PM",
        "Gratis",
        R.drawable.combeima,
        Color(0xFFAED8C0),
        Icons.Outlined.Landscape,
        4.4520,
        -75.2050
    ),
    IbaguePlace(
        "san-jorge",
        "Jardín Botánico San Jorge",
        "Calle 10 #8-07, Ibagué",
        "Un refugio de naturaleza con senderos y colecciones de flora que invita a conocer los ecosistemas del Tolima.",
        4.8,
        "Mar - Dom 8:00 AM - 4:00 PM",
        "$8.000 COP",
        R.drawable.jardinsanjorge,
        Color(0xFFC5DEA2),
        Icons.Outlined.Park,
        4.4535,
        -75.2210
    ),
    IbaguePlace(
        "conservatorio",
        "Conservatorio del Tolima",
        "Cra. 3 #11-76, Ibagué",
        "Patrimonio musical de la ciudad y una institución dedicada a formar generaciones de músicos colombianos.",
        4.7,
        "Lun - Vie 8:00 AM - 5:00 PM",
        "Consultar agenda",
        R.drawable.conservatorio,
        Color(0xFFF4C77B),
        Icons.Outlined.MusicNote,
        4.4436,
        -75.2444
    ),
    IbaguePlace(
        "plaza-bolivar",
        "Plaza de Bolívar de Ibagué",
        "Centro histórico de Ibagué",
        "La plaza principal de la ciudad, rodeada de edificios históricos y un punto de encuentro para la vida cultural ibaguereña.",
        4.7,
        "Abierto 24 horas",
        "Gratis",
        R.drawable.plazabolivar,
        Color(0xFFB9D4EC),
        Icons.Outlined.Place,
        4.4446,
        -75.2430
    ),
    IbaguePlace(
        "parque-musica",
        "Parque de la Música",
        "Cra. 1 con Calle 10, Ibagué",
        "Espacio al aire libre dedicado a la tradición musical de Ibagué, con escenarios y zonas para descansar.",
        4.8,
        "Todos los días 6:00 AM - 10:00 PM",
        "Gratis",
        R.drawable.parquedelamusica,
        Color(0xFFD8C6F0),
        Icons.Outlined.AccountBalance,
        4.4432,
        -75.2442
    )
)