package com.example.colombiaturismo.pereira

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Church
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Terrain
import androidx.compose.ui.graphics.Color
import com.example.colombiaturismo.PlaceCategory
import com.example.colombiaturismo.R

// Coordenadas tomadas de Wikipedia y OpenStreetMap. Fotos de Wikimedia Commons con licencia
// Creative Commons; los créditos están en imageCredit y en CREDITOS_IMAGENES.md.
val pereiraPlaces = listOf(
    PereiraPlace(
        id = "bolivar-desnudo",
        name = "Plaza de Bolívar y Bolívar Desnudo",
        location = "Plaza de Bolívar, Centro",
        description = "El corazón cívico de Pereira. En la plaza se levanta el Bolívar Desnudo, escultura en bronce de Rodrigo Arenas Betancourt inaugurada en 1963 para el centenario de la ciudad: mide unos 9 metros de alto, pesa cerca de 14 toneladas y muestra al Libertador desnudo sobre su caballo. Polémica en su momento, hoy es el gran símbolo de la capital de Risaralda.",
        rating = 4.7,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.pereira_bolivar_desnudo,
        imageCredit = "Foto: Petruss · CC BY-SA 4.0",
        placeholderColor = Color(0xFFF4C77B),
        icon = Icons.Outlined.Place,
        latitude = 4.8144,
        longitude = -75.6947,
        categories = setOf(PlaceCategory.HISTORIA, PlaceCategory.CULTURA)
    ),
    PereiraPlace(
        id = "catedral",
        name = "Catedral de Nuestra Señora de la Pobreza",
        location = "Carrera 7 entre calles 20 y 21",
        description = "Templo de planta basilical en el costado occidental de la Plaza de Bolívar, terminado en 1890 y reconstruido tras los terremotos de 1906 y 1999. Su tesoro está adentro: un artesonado de unas 13.000 piezas de madera de comino crespo que sostiene la cúpula, junto a muros de ladrillo a la vista decorados con mosaicos de aire bizantino.",
        rating = 4.8,
        schedule = "Abierta en horarios de culto",
        price = "Entrada libre",
        imageRes = R.drawable.pereira_catedral,
        imageCredit = "Foto: SajoR · CC BY-SA 4.0",
        placeholderColor = Color(0xFFD8C6F0),
        icon = Icons.Outlined.Church,
        latitude = 4.81465,
        longitude = -75.69536,
        categories = setOf(PlaceCategory.HISTORIA, PlaceCategory.CULTURA)
    ),
    PereiraPlace(
        id = "viaducto",
        name = "Viaducto César Gaviria Trujillo",
        location = "Entre Pereira y Dosquebradas",
        description = "Puente atirantado de concreto inaugurado en 1997 que une a Pereira con Dosquebradas sobre el río Otún. Con 704 metros de longitud y una luz central de 211 metros, es una de las obras de ingeniería más reconocidas del país y una de las postales obligadas de la ciudad.",
        rating = 4.6,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.pereira_viaducto,
        imageCredit = "Foto: ledpup · CC BY-SA 2.0",
        placeholderColor = Color(0xFFB9D4EC),
        icon = Icons.Outlined.Terrain,
        latitude = 4.81778,
        longitude = -75.68583,
        categories = setOf(PlaceCategory.CULTURA)
    ),
    PereiraPlace(
        id = "lago-uribe",
        name = "Parque El Lago Uribe Uribe",
        location = "Calles 24 y 25 con carreras 7 y 8",
        description = "Uno de los parques más tradicionales del centro, conocido antes como Plaza de la Concordia y Plazuela de Colón. Su lago, rodeado de pérgolas y árboles, es un respiro en medio de la ciudad, y a su alrededor se reúnen cafés, bares de tango y restaurantes de ambiente bohemio. En su costado norte se alza la Iglesia El Claret.",
        rating = 4.5,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.pereira_lago_uribe,
        imageCredit = "Foto: rothware · CC BY-SA 3.0",
        placeholderColor = Color(0xFFC5DEA2),
        icon = Icons.Outlined.Park,
        latitude = 4.8148,
        longitude = -75.6989,
        categories = setOf(PlaceCategory.NATURALEZA, PlaceCategory.HISTORIA)
    ),
    PereiraPlace(
        id = "otun-quimbaya",
        name = "Santuario de Fauna y Flora Otún Quimbaya",
        location = "Vereda La Suiza, cuenca del río Otún",
        description = "Área protegida de Parques Nacionales creada en 1996 en la cordillera Central, entre 1.800 y 2.400 metros de altitud. Sus senderos de bosque andino junto al río Otún permiten observar aves, mariposas, pequeños mamíferos y la palma de cera, árbol nacional de Colombia. Ideal para caminatas ecológicas y avistamiento de aves.",
        rating = 4.8,
        schedule = "Visitas con reserva previa",
        price = "Consultar tarifa en Parques Nacionales",
        imageRes = R.drawable.pereira_otun_quimbaya,
        imageCredit = "Foto: Torressergio96 · CC BY-SA 4.0",
        placeholderColor = Color(0xFFAED8C0),
        icon = Icons.Outlined.Landscape,
        latitude = 4.72361,
        longitude = -75.57917,
        categories = setOf(PlaceCategory.NATURALEZA)
    )
)
