# Colombia Turismo (Colom-Via)

Aplicación Android en Kotlin + Jetpack Compose para el catálogo turístico de la universidad. Permite explorar los principales sitios turísticos de varias ciudades de Colombia, ver su información detallada y ubicarlos en el mapa. El diseño se basa en el mockup HTML entregado.

## Funcionalidades

- **Inicio de sesión y registro**: creación de cuenta con nombre, correo y contraseña, validación de los campos y recuperación de contraseña.
- **Sesión persistente**: el usuario permanece conectado al cerrar y volver a abrir la app hasta que cierre sesión.
- **Catálogo por ciudades**: Bogotá, Cartagena, Ibagué y Medellín, seleccionables desde la columna lateral de la pantalla de inicio.
- **Buscador y filtros** por categoría: Todos, Cultura, Naturaleza, Historia y Playas.
- **Detalle de cada lugar**: imagen, descripción, ubicación, horario, precio y calificación.
- **Ubicación en el mapa**: cada lugar abre su ubicación en Google Maps a partir de sus coordenadas.
- **Perfil**: consulta de los datos del usuario, edición del nombre y cambio de contraseña.
- **Navegación**: menú lateral (hamburguesa) con Home y Cerrar sesión, y barra inferior con Explorar, Favoritos y Perfil.

## Tecnologías

- Kotlin 2.2.20 y Jetpack Compose (Material 3)
- Android Gradle Plugin 8.13.0
- Room para guardar los usuarios de forma local
- DataStore para la sesión activa
- Contraseñas almacenadas con hash y sal
- JUnit 4 para las pruebas unitarias

## Requisitos

- Android Studio actualizado
- JDK 17
- Dispositivo o emulador con Android 7.0 (API 24) o superior

## Estructura del proyecto

```
app/src/main/kotlin/com/example/colombiaturismo/
├── MainActivity.kt      Pantalla de inicio, navegación, menú lateral y barra inferior
├── ColomViaApp.kt       Inicialización de la base de datos y la sesión
├── auth/                Login, registro y recuperación de contraseña
├── profile/             Perfil del usuario y cierre de sesión
├── data/                Base de datos (Room), sesión (DataStore) y autenticación
├── bogota/              Datos, lista y detalle de Bogotá
├── cartagena/           Datos, lista y detalle de Cartagena
├── ibague/              Datos, lista y detalle de Ibagué
├── medellin/            Datos, lista y detalle de Medellín
└── ui/theme/            Colores de la app
```

Las imágenes de los lugares están en `app/src/main/res/drawable/`.

## Cómo ejecutar el proyecto

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/yeisondeviapoli/ColombiaTurismo.git
   ```

2. Abrir Android Studio → **Open** y seleccionar la carpeta `ColombiaTurismo`.
3. Esperar a que termine el Gradle Sync.
4. Ejecutar la app en un emulador o teléfono.

También se puede compilar desde la terminal:

```bash
./gradlew assembleDebug    # genera el APK en app/build/outputs/apk/debug/
./gradlew installDebug     # instala la app en el dispositivo conectado
./gradlew test             # ejecuta las pruebas unitarias
```

En Windows (PowerShell) se usa `.\gradlew.bat` en lugar de `./gradlew`.

## Trabajo en equipo

Cada integrante trabaja en su propia rama creada desde `main` actualizado y luego abre un Pull Request hacia `main`.

```bash
git checkout main
git pull
git checkout -b feature/nombre-del-cambio
git add .
git commit -m "feat: descripción del cambio"
git push -u origin feature/nombre-del-cambio
```

Convención de nombres:

- `feature/...` para funcionalidades nuevas
- `fix/...` para correcciones
