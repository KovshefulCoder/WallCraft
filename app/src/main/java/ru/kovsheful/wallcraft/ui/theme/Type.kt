package ru.kovsheful.wallcraft.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.kovsheful.wallcraft.R

val roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_italic, FontWeight.Normal),
    Font(R.font.roboto_light, FontWeight.Light),
)

val typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = TextColor,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    titleLarge = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = TextColor,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    bodyMedium = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = TextColor,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    bodySmall = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextColor,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    labelMedium = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        color = TextColor,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
)