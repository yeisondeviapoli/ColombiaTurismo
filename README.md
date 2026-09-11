# Colombia Turismo

Aplicación Android en Kotlin + Jetpack Compose para el catálogo turístico de la universidad.

## Funcionalidad actual

- Pantalla inicial inspirada en el mockup HTML entregado.
- Catálogo de ciudades.
- Navegación desde Inicio hacia Ibagué.
- Página de Ibagué con lugares destacados.
- Datos estáticos, sin base de datos.
- Arquitectura sencilla para que diferentes integrantes puedan modificar contenido.

## Abrir en Android Studio

1. Descomprimir el proyecto.
2. Android Studio → Open.
3. Seleccionar la carpeta `ColombiaTurismo`.
4. Esperar el Gradle Sync.
5. Ejecutar en un emulador o teléfono.

## Git / trabajo en equipo

Después de crear el repositorio en GitHub:

```bash
git init
git add .
git commit -m "feat: base inicial del catalogo turistico"
git branch -M main
git remote add origin https://github.com/TU-USUARIO/TU-REPOSITORIO.git
git push -u origin main
```

Para trabajo en equipo:

```bash
git checkout -b feature/ibague
git add .
git commit -m "feat: pagina de ibague"
git push -u origin feature/ibague
```

Cada integrante debería trabajar en su propia rama y luego hacer Pull Request hacia `main`.
