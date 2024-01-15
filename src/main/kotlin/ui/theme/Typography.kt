package ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val interFontFamily = FontFamily(
    Font(
        resource = "fonts/inter/inter_light.ttf",
        weight = FontWeight.Light,
    ),
    Font(
        resource = "fonts/inter/inter_regular.ttf",
        weight = FontWeight.Normal,
    ),
    Font(
        resource = "fonts/inter/inter_medium.ttf",
        weight = FontWeight.Medium,
    ),
    Font(
        resource = "fonts/inter/inter_semi_bold.ttf",
        weight = FontWeight.SemiBold,
    ),
    Font(
        resource = "fonts/inter/inter_bold.ttf",
        weight = FontWeight.Bold,
    ),
)

//TODO: Finish typography styles
val typography = Typography(
    defaultFontFamily = interFontFamily,
    button = TextStyle(
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Medium,
    ),
    h1 = TextStyle(
        letterSpacing = 0.sp,
    ),
    h2 = TextStyle(
        letterSpacing = 0.sp,
    ),
    h3 = TextStyle(
        letterSpacing = 0.sp,
    ),
    h4 = TextStyle(
        letterSpacing = 0.sp,
    ),
    h5 = TextStyle(
        letterSpacing = 0.sp,
    ),
    h6 = TextStyle(
        letterSpacing = 0.sp,
    ),
    subtitle1 = TextStyle(
        letterSpacing = 0.sp,
    ),
    subtitle2 = TextStyle(
        letterSpacing = 0.sp,
    ),
    body1 = TextStyle(
        letterSpacing = 0.sp,
    ),
    body2 = TextStyle(
        letterSpacing = 0.sp,
    ),
    caption = TextStyle(
        letterSpacing = 0.sp,
    ),
    overline = TextStyle(
        letterSpacing = 0.sp,
    ),
)