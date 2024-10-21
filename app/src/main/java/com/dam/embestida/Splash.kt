package com.dam.embestida

import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun Splash(onAnimationEnd: () -> Unit, onSplashExit: () -> Unit) {
    val context = LocalContext.current // Necesario para reproducir sonido

    // Estado inicial para controlar cuándo empezar la animación
    var startAnimation by remember { mutableStateOf(false) }

    // Estado para controlar cuándo empezar la animación de salida
    var startExitAnimation by remember { mutableStateOf(false) }

    // Estado para controlar el progreso de la animación
    val shieldScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 3f,
        animationSpec = tween(700, delayMillis = 0, easing = EaseOutElastic),
        label = "Shield Scale"
    )
    val shieldAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(400, delayMillis = 0, easing = EaseOutElastic),
        label = "Shield Alpha"
    )
    val titleScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 3f,
        animationSpec = tween(700, delayMillis = 1000, easing = EaseOutElastic),
        label = "Title Scale"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(400, delayMillis = 1000, easing = EaseOutElastic),
        label = "Title Alpha"
    )
    val splashRotation by animateFloatAsState(
        targetValue = if (startExitAnimation) -90f else 0f, // Rotar 90 grados
        animationSpec = tween(durationMillis = 700, easing = EaseInElastic),
        label = "Splash Rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-100).dp)
            .graphicsLayer(
                rotationZ = splashRotation, // Rotación en el eje Z
                transformOrigin = TransformOrigin(
                    0.5f,
                    1f
                ) // Origen de rotación en el centro y abajo (parte inferior)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de un escudo con dos espadas cruzadas de fondo y una interrogación en meido",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxWidth()
                .scale(shieldScale)
                .alpha(shieldAlpha)
        )
        Spacer(modifier = Modifier.height(80.dp)) // Espaciado entre escudo y título
        Image(
            painter = painterResource(id = R.drawable.title),
            contentDescription = "Titulo dice: Estampida",
            contentScale = ContentScale.None,
            modifier = Modifier
                .scale(titleScale)
                .alpha(titleAlpha)
        )
    }


    // Inicia la animación automáticamente cuando se carga el Composable
    LaunchedEffect(Unit) {
        startAnimation = true // Activa la animación
    }

    // Reproduce el sonido cuando el escudo llega a su posición final
    LaunchedEffect(shieldAlpha) {
        if (shieldAlpha == 1f) {
            playDemolitionSound(context)
        }
    }

    // Reproduce el sonido cuando el título llega a su posición final
    LaunchedEffect(titleAlpha) {
        if (titleAlpha == 1f) {
            playDemolitionSound(context)
        }
    }

    // Controlamos cuándo finaliza la animación
    LaunchedEffect(key1 = shieldAlpha, key2 = titleScale) {
        if (shieldAlpha == 1f && titleScale == 1f) {
            // Llamamos al callback `onAnimationEnd` cuando ambas animaciones finalizan
            delay(1300) // Esperamos un segundo para asegurarnos de que la animación ha finalizado
            startExitAnimation = true // Inicia la animación de salida
            onAnimationEnd() // Llamamos al callback para mostrar el menú principal
        }
    }

    LaunchedEffect(splashRotation) {
        if (splashRotation == 90f) {
            onSplashExit() // Llamamos al callback para ocultar la pantalla de splash
        }
    }

}

