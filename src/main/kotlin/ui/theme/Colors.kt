package ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


val lightColors = lightColors(
    primary = Color(0xff18181b),
    primaryVariant = Color(0xff18181b),
    secondary = Color(0xfff4f4f5),
    secondaryVariant = Color(0xfff4f4f5),
    background = Color.White,
    surface = Color.White,
    error = Color(0xffef4444),
    onPrimary = Color.White,
    onSecondary = Color(0xff09090b),
    onBackground = Color(0xff09090b),
    onSurface = Color(0xff09090b),
    onError = Color(0xfffaf7f4),
)

val darkColors = darkColors(
    primary = Color(0xfffafafa),
    primaryVariant = Color(0xfffafafa),
    secondary = Color(0xff27272a),
    secondaryVariant = Color(0xff27272a),
    background = Color(0xff09090b),
    surface = Color(0xff09090b),
    error = Color(0xff7f1d1d),
    onPrimary = Color(0xff20181b),
    onSecondary = Color(0xff20181b),
    onBackground = Color(0xfffafafa),
    onSurface = Color(0xfffafafa),
    onError = Color(0xfffafafa),
)
