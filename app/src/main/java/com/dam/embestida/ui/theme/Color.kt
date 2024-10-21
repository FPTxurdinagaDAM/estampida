package com.dam.embestida.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

fun gradienteIridiscenteClaro(): Brush {
    return Brush.sweepGradient(
        colors = listOf(
            Color.hsv(
                36f,
                0.3f,
                0.97f
            ),  // Dorado claro (matiz = 36º, saturación = 0.3, luminosidad = 0.9)
            Color.hsv(
                30f,
                0.2f,
                0.94f
            ), // Naranja suave (matiz = 30º, saturación = 0.2, luminosidad = 0.85)
            Color.hsv(
                340f,
                0.25f,
                0.97f
            ), // Rosa pálido (matiz = 340º, saturación = 0.25, luminosidad = 0.88)
            Color.hsv(
                270f,
                0.2f,
                0.95f
            ), // Violeta grisáceo (matiz = 270º, saturación = 0.2, luminosidad = 0.85)
            Color.hsv(
                210f,
                0.2f,
                0.97f
            ), // Azul grisáceo claro (matiz = 210º, saturación = 0.2, luminosidad = 0.9)
            Color.hsv(
                150f,
                0.2f,
                0.95f
            )  // Verde pálido (matiz = 150º, saturación = 0.2, luminosidad = 0.85)
        ),
        center = Offset(980f, 600f),
    )
}

fun gradienteIridiscenteSaturado(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color(0xFFE6D5B8), // Dorado claro desaturado
            Color(0xFFD9A47B), // Naranja suave desaturado
            Color(0xFFC1858C), // Rosa pálido
            Color(0xFFB3A1C6), // Violeta grisáceo
            Color(0xFF80B3C4), // Azul grisáceo suave
            Color(0xFF92C5A0)  // Verde pálido suave
        )
    )
}