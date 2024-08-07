package de.lucas.cookingrecipes.main.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Dark,
    onPrimary = Color.White,
    secondary = DarkGrey,
    onSecondary = Color.White,
    tertiary = TranslucentGrey,
    onTertiary = Color.White,
    primaryContainer = DarkerGrey,
    onPrimaryContainer = Color.White,
)

@Composable
fun CookingRecipesTheme(
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}