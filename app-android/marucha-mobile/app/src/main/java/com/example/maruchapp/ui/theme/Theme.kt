package com.example.maruchapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,

    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,

    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,

    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,

    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,

    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,

    outline = md_theme_dark_outline,

    error = md_theme_dark_error,
    onError = md_theme_dark_onError
)

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,

    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,

    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,

    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,

    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,

    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,

    outline = md_theme_light_outline,

    error = md_theme_light_error,
    onError = md_theme_light_onError
)

@Composable
fun MaruchAPPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // IMPORTANTE: desactivado para respetar el tema Marucha
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}