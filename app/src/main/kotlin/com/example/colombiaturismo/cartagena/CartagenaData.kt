package com.example.colombiaturismo.cartagena

import androidx.annotation.DrawableRes
import com.example.colombiaturismo.R

// Definimos la clase incluyendo el recurso de la imagen
data class CartagenaPlace(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val rating: Double,
    val schedule: String,
    val price: String,
    @DrawableRes val imageRes: Int // Referencia al recurso de imagen en drawable
)

// Lista con los 5 sitios principales de Cartagena con descripciones extendidas
val cartagenaPlaces = listOf(
    CartagenaPlace(
        id = "1",
        name = "Ciudad Amurallada",
        location = "Centro Histórico",
        description = "Declarada Patrimonio de la Humanidad por la UNESCO, la Ciudad Amurallada es el corazón histórico de Cartagena. Está rodeada por imponentes murallas defensivas construidas durante la época colonial para proteger la ciudad de ataques piratas. Sus calles empedradas están llenas de coloridas casas coloniales adornadas con buganvilias, plazas vibrantes, iglesias centenarias y una oferta gastronómica y cultural excepcional que cautiva a cada visitante.",
        rating = 4.9,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.ciudad_amurallada
    ),
    CartagenaPlace(
        id = "2",
        name = "Castillo de San Felipe",
        location = "Pie del Cerro, Cra. 17",
        description = "Considerada la mayor obra de ingeniería militar española en América, esta formidable fortaleza fue construida en el siglo XVII sobre el cerro San Lázaro. Cuenta con un complejo laberinto de túneles subterráneos, minas y puestos estratégicos de observación diseñados para hacerla inexpugnable. Recorrer sus rampas ofrece una inmersión profunda en las batallas históricas y una vista privilegiada de toda la ciudad.",
        rating = 4.8,
        schedule = "8:00 AM - 6:00 PM",
        price = "$30.000 COP",
        imageRes = R.drawable.san_felipe
    ),
    CartagenaPlace(
        id = "3",
        name = "Islas del Rosario",
        location = "Archipiélago del Rosario",
        description = "Un paraíso natural conformado por un pequeño archipiélago de islas coralinas e islotes ubicados frente a las costas de Cartagena. Sus aguas cristalinas de tonos turquesa y sus arrecifes de coral albergan una biodiversidad marina impresionante, convirtiéndolo en el destino perfecto para practicar esnórquel, buceo, relajarse en playas de arena blanca y disfrutar de la gastronomía típica caribeña a base de pescados frescos.",
        rating = 4.9,
        schedule = "Salidas 7:00 AM",
        price = "Varía según tour",
        imageRes = R.drawable.islas_rosario
    ),
    CartagenaPlace(
        id = "4",
        name = "Playa de Bocagrande",
        location = "Avenida San Martín, Bocagrande",
        description = "La zona costera más moderna y cosmopolita de Cartagena. Ubicada frente al mar Caribe, combina imponentes rascacielos hoteleros, centros comerciales, restaurantes exclusivos y una animada vida nocturna. Su extensa franja de arena y oleaje la convierten en un punto de encuentro muy concurrido tanto para locales como para turistas que buscan sol, mar y entretenimiento a pocos minutos del centro histórico.",
        rating = 4.5,
        schedule = "Abierto 24 horas",
        price = "Gratis",
        imageRes = R.drawable.bocagrande
    ),
    CartagenaPlace(
        id = "5",
        name = "Convento de la Popa",
        location = "Alto de La Popa",
        description = "Situado en la cima del cerro de La Popa —el punto más alto de Cartagena a unos 150 metros sobre el nivel del mar se encuentra este histórico convento e iglesia consagrada a la Virgen de la Candelaria. Fundado a principios del siglo XVII por agustinos recoletos, ofrece sin duda el mirador panorámico de 360 grados más impresionante de toda la ciudad, abarcando la bahía, el mar Caribe y los techos coloniales.",
        rating = 4.7,
        schedule = "8:00 AM - 5:00 PM",
        price = "$20.000 COP",
        imageRes = R.drawable.convento_popa
    )
)