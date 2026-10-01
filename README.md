# Kotlin-listado-de-alumnos

Aplicación nativa para Android desarrollada en Kotlin usando Android Studio.

## Características
- **RecyclerView**: Lista dinámica de alumnos.
- **Base de Datos SQLite (`DBHelperAlumno`)**: Almacenamiento persistente local con operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
- **Glide**: Carga y renderizado eficiente de imágenes de perfil desde URLs.
- **Diseño con Material Design**: Interfaz moderna basada en tarjetas (`MaterialCardView`) y botones flotantes.

## Atributos de la clase Alumno
- `nombre`: Nombre completo del alumno.
- `cuenta`: Número de cuenta (Llave primaria en SQLite).
- `correo`: Correo electrónico.
- `imagen`: URL de la foto/imagen de perfil.
