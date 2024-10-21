package com.dam.embestida.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.dam.embestida.R

// Define tu familia de fuentes
val YouBlockhead = FontFamily(
    Font(R.font.youblockhead, FontWeight.Normal), // Carga la fuente normal
)

val YouBlockheadOpen = FontFamily(
    Font(R.font.youblockheadopen, FontWeight.Normal), // Carga la fuente normal
)

val PinkChicken = FontFamily(
    Font(R.font.pink_chicken_regular, FontWeight.Normal), // Carga la fuente normal
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)