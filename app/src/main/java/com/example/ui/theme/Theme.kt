package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    primaryContainer = OrangeLight,
    onPrimaryContainer = OrangeDark,
    secondary = CivicBlue,
    onSecondary = Color.White,
    secondaryContainer = CivicBlueLight,
    onSecondaryContainer = CivicBlue,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF8F9FA),
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3E1D00),
    onPrimaryContainer = Color(0xFFFFCC99),
    secondary = CivicBlue,
    onSecondary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF282828),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF333333)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded orange aesthetic consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
