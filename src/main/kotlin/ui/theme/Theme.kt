package ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AlpacaTheme(
    colors: Colors = lightColors,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content,
    )
}
