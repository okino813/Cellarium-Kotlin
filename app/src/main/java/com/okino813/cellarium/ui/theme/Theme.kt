package com.okino813.cellarium.ui.theme

import android.app.Activity
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

private val DarkColorScheme = darkColorScheme(
    primary = Rouge80,
    onPrimary = Color(0xFF680016),
    primaryContainer = Color(0xFF930026),
    onPrimaryContainer = Color(0xFFFFDAD9),

    secondary = Gris80,
    onSecondary = Color(0xFF263236),
    secondaryContainer = Color(0xFF3C484C),
    onSecondaryContainer = Color(0xFFDBE4E8),

    tertiary = Ambre80,
    onTertiary = Color(0xFF422C00),
    tertiaryContainer = Color(0xFF5D4100),
    onTertiaryContainer = Color(0xFFFFDDB3),

    background = Color(0xFF1A1110),  // fond très sombre teinté rouge
    onBackground = Color(0xFFF0DEDD),
    surface = Color(0xFF211918),
    onSurface = Color(0xFFF0DEDD),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

private val LightColorScheme = lightColorScheme(
    primary = Rouge40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD9),
    onPrimaryContainer = Color(0xFF410008),

    secondary = Gris40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDBE4E8),
    onSecondaryContainer = Color(0xFF1C2B2F),

    tertiary = Ambre40,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDDB3),
    onTertiaryContainer = Color(0xFF271900),

    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF201A1A),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF201A1A),
    surfaceVariant = Color(0xFFF4DDDD),
    onSurfaceVariant = Color(0xFF534343),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
)

@Composable
fun CellariumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}