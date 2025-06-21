// Lokasi: app/src/main/java/com/example/loker/ui/theme/Theme.kt
package com.example.loker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Skema warna untuk mode terang
private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    secondary = DarkPurple,
    background = Color.White,
    surface = SurfaceGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextBlack,
    onSurface = TextBlack,
    outline = BorderGray,
    error = Color(0xFFB00020)
)

// TODO: Nanti Anda bisa membuat DarkColorScheme untuk mode gelap

@Composable
fun LokerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Untuk saat ini kita hanya pakai mode terang

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}