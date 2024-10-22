package com.dam.embestida

import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dam.embestida.ui.theme.YouBlockhead


@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current // Necesario para reproducir la música
    var hideSplash by remember { mutableStateOf(false) } // Controla cuándo mostrar el menú principal
    var showMenu by remember { mutableStateOf(false) } // Controla cuándo mostrar el menú principal
    val splashEnabled by remember { mutableStateOf(true) } // Controla cuándo mostrar el splash (En desarrollo puede hacer perder unos segundos cada ejecución)
    if (!splashEnabled){
        showMenu = true // Ocultar el menú principal si se muestra la splash
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = "Splash Screen",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (splashEnabled && !hideSplash)
            Splash(
                onAnimationEnd = {
                    showMenu = true // Activa el menú cuando termine la animación de la splash
                },
                onSplashExit = {
                    hideSplash = true // Oculta la splash cuando termine la animación
                }
            )

        // Mostrar menú principal con animación
        if (showMenu) {
            MainMenu(navController)
            PlayHomeMusic(context) // Reproducir música de fondo
        }

    }


}


@Composable
fun MainMenu(navController: NavHostController) {

    // Estado para controlar el inicio de la animación
    var startMenuAnimation by remember { mutableStateOf(false) }

    // Animación de rotación para el menú, que se ejecuta una sola vez
    val menuRotation by animateFloatAsState(
        targetValue = if (startMenuAnimation) 0f else 90f, // Rotar desde -90 grados hasta 0
        animationSpec = tween(durationMillis = 700, delayMillis = 700, easing = EaseOutElastic),
        label = "Menu Rotation"
    )

    // Menú principal con animación de rotación
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                rotationZ = menuRotation, // Rotación en el eje Z
                transformOrigin = TransformOrigin(
                    0.5f,
                    1f
                ) // Origen de rotación en el centro y abajo (parte inferior)
            ), // Aparece rotando desde la parte inferior
        contentAlignment = Alignment.Center // Centrar el contenido
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(16.dp)
                .offset(y = (-100).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.title),
                contentDescription = "Titulo",
                contentScale = ContentScale.None,
                modifier = Modifier.scale(0.8f)
            )

            // Botón para iniciar el juego
            PlayMenuButton(onClick = {
                navController.navigate(AppScreen.GameScreen.route)
            })
        }
    }

    // Iniciar la animación del menú cuando se carga el composable
    LaunchedEffect(Unit) {
        startMenuAnimation = true
    }
}

@Composable
fun PlayMenuButton(onClick: () -> Unit = {}) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tablero),
            contentDescription = "Tablon de madera",
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier.offset(x = (5).dp)
        )
        Button(
            onClick = onClick,
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )
        ) {
            // Aquí puedes agregar el contenido del botón
            Text(
                "Jugar", fontSize = 40.sp, color = Color.White, fontFamily = YouBlockhead
            )
        }
    }
}