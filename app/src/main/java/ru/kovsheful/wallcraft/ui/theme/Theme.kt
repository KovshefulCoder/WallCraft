package ru.kovsheful.wallcraft.ui.theme

import android.app.Activity
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF286EF1),
    background = Color(0xFF17171A),
    onBackground = Color.White,
    secondary = Color(0xFF1E1E29),
    onSecondary = Color(0xFF90949F),
    error = Color(0xFFF06E6D)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF286EF1),
    background = Color.White,
    onBackground = Color(0xFF17171A),
    secondary = Color(0xFF90949F),
    onSecondary = Color(0xFF1E1E29),
    error = Color(0xFFF06E6D)
)

@Composable
fun WallCraftCleanArchitectureTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}