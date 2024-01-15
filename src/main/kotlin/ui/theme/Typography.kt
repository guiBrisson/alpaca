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

val typography = Typography(
    defaultFontFamily = interFontFamily,
    button = TextStyle(
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Medium,
    )
)