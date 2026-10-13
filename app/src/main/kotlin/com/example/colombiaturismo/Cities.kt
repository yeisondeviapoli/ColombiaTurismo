package com.example.colombiaturismo

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Información básica de una ciudad (RF04, clase Ciudad del diagrama). */
data class CityInfo(
    val name: String,
    val department: String,
    /** Apodo que muestra el mockup en el menú de ciudades. */
    val nickname: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val imageColor: Color,
    val icon: ImageVector
) {
    /** "Cartagena, Bolívar", como en las tarjetas del mockup. */
    val region: String get() = if (department.isEmpty()) name else "$name, $department"
}

val colombiaCities = listOf(
    CityInfo(
        name = "Bogotá",
        department = "",
        nickname = "La capital",
        description = "La capital del país, a más de 2.600 metros de altura en la sabana. Combina un centro histórico colonial, museos de talla mundial y una vida cultural que no se detiene.",
        imageRes = R.drawable.bogota_ciudad,
        imageColor = Color(0xFF85B7EB),
        icon = Icons.Outlined.AccountBalance
    ),
    CityInfo(
        name = "Medellín",
        department = "Antioquia",
        nickname = "La ciudad de la eterna primavera",
        description = "Capital de Antioquia en el Valle de Aburrá, conocida por su clima templado todo el año, su transformación urbana, el Metrocable y la Feria de las Flores.",
        imageRes = R.drawable.pueblitopaisa,
        imageColor = Color(0xFFC0DD97),
        icon = Icons.Outlined.Business
    ),
    CityInfo(
        name = "Cartagena",
        department = "Bolívar",
        nickname = "La heroica",
        description = "Ciudad amurallada del Caribe, Patrimonio de la Humanidad por la UNESCO. Historia colonial, fortalezas, playas e islas a pocos minutos del centro.",
        imageRes = R.drawable.ciudad_amurallada,
        imageColor = Color(0xFFF5C4B3),
        icon = Icons.Outlined.Museum
    ),
    CityInfo(
        name = "Ibagué",
        department = "Tolima",
        nickname = "Capital musical",
        description = "La capital musical de Colombia, al pie de la cordillera Central. Sede del Conservatorio del Tolima y puerta de entrada al Cañón del Combeima.",
        imageRes = R.drawable.combeima,
        imageColor = Color(0xFFD8C6F0),
        icon = Icons.Outlined.MusicNote
    ),
    CityInfo(
        name = "Pereira",
        department = "Risaralda",
        nickname = "Corazón del eje cafetero",
        description = "Capital de Risaralda en pleno Eje Cafetero, región del Paisaje Cultural Cafetero declarado Patrimonio de la Humanidad. Ciudad cálida y comercial rodeada de naturaleza.",
        imageRes = R.drawable.pereira_viaducto,
        imageColor = Color(0xFFAED8C0),
        icon = Icons.Outlined.Park
    )
)

fun cityInfo(name: String): CityInfo = colombiaCities.first { it.name == name }

/** Encabezado con la información básica de la ciudad, al inicio de cada lista de lugares. */
@Composable
fun CityHeader(city: CityInfo) {
    Column(Modifier.padding(start = 4.dp, bottom = 2.dp)) {
        Text(city.name, color = Color(0xFF1F3F5C), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(city.nickname, color = Color(0xFF7A8FA3), fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(
            city.description,
            color = Color(0xFF7A8FA3),
            fontSize = 12.sp,
            lineHeight = 17.sp,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
