# MaruchAPP Android

Aplicación móvil Android de **Marucha**, desarrollada en **Kotlin + Jetpack Compose**, conectada al backend PHP del proyecto **Mobile-Application-for-Restaurant-Marucha-**.

Actualmente, esta app corresponde a la base inicial del cliente móvil e incluye:

- estructura base del proyecto Android
- navegación entre pantallas
- conexión con el backend local
- login funcional contra la API PHP

---

# Estado actual del módulo Android

## Versión actual implementada
**v0.9.0**

## Funcionalidades disponibles
- Splash screen
- Login screen
- Home screen base
- Profile screen base
- navegación con Navigation Compose
- conexión con backend local mediante Retrofit
- login real consumiendo `POST /api/auth/login`

---

# Tecnologías usadas

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Navigation Compose**
- **Retrofit**
- **Gson Converter**
- **OkHttp**
- **Coil** (preparado para futuras imágenes)
- **Android Studio**

---

# Estructura general del módulo Android

```bash
app-android/
└── app/
    └── src/
        └── main/
            ├── java/com/example/maruchapp/
            │   ├── data/
            │   │   ├── model/
            │   │   └── remote/
            │   ├── ui/
            │   │   ├── navigation/
            │   │   ├── screens/
            │   │   │   ├── auth/
            │   │   │   ├── home/
            │   │   │   ├── profile/
            │   │   │   └── splash/
            │   │   └── theme/
            │   └── MainActivity.kt
            └── res/
                └── xml/
                    └── network_security_config.xml