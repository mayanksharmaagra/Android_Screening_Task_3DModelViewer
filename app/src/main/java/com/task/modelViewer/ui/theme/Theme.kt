package com.task.modelViewer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


private val DarkColorScheme = darkColorScheme(
    primary     = Color(0xFF82B1FF),
    secondary   = Color(0xFF80CBC4),
    background  = Color(0xFF121212),
    surface     = Color(0xFF1E1E1E),
    onPrimary   = Color.Black,
    onSecondary = Color.Black,
    onBackground= Color.White,
    onSurface   = Color.White,
)

@Composable
fun ModelViewerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content     = content
    )
}