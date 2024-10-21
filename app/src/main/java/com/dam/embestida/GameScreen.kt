package com.dam.embestida

import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dam.embestida.ui.theme.PinkChicken
import com.dam.embestida.ui.theme.YouBlockhead
import com.dam.embestida.ui.theme.gradienteIridiscenteClaro
import com.dam.embestida.ui.theme.gradienteIridiscenteSaturado


@Composable
fun GameScreen(navController: NavHostController) {
    var context = LocalContext.current // Necesario para reproducir la música

    /**
     *     viewModel(): En lugar de crear el GameViewModel manualmente con
     *     val viewModel = GameViewModel(), ahora utilizas val viewModel: GameViewModel = viewModel().
     *     Esta función asegura que el ViewModel sea compartido entre las recomposiciones
     *     solo se cree una vez por el ciclo de vida del Composable.
     *     val viewModel: GameViewModel = viewModel()
     */
    val viewModel: GameViewModel = viewModel()

    val musica: Int by viewModel.musica.collectAsState()

    PlayGameMusic(context, musica) // Reproducir la música de fondo

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = "Splash Screen",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarArea(onClick = {
                viewModel.abandonarJuego()
                navController.popBackStack()
            })
            QuestionArea(viewModel)
        }
    }
}

@Composable
fun TopBarArea(onClick: () -> Unit = {}) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(15.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(80.dp)
                .background(Color.Transparent),
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {

                // Imagen que actúa como botón
                Image(
                    painter = painterResource(id = R.drawable.escudo_redondo), // Reemplaza con tu recurso de imagen
                    contentDescription = "Timón de madera",
                    modifier = Modifier.size(80.dp) // Tamaño del botón
                )

                Icon(
                    painter = painterResource(id = R.drawable.rounded_exit_to_app_24), // Reemplaza con tu recurso de imagen
                    contentDescription = "Flecha de regreso",
                    modifier = Modifier.size(40.dp), // Tamaño del botón
                    tint = Color.hsv(
                        0f, 0f, 0.9f
                    ) // Color del icono

                )
            }

        }
    }
}

@Composable
fun QuestionArea(viewModel: GameViewModel) {

    val pregunta: Pregunta by viewModel.pregunta.observeAsState(initial = Pregunta())
    val racha: Int by viewModel.racha.observeAsState(initial = 0)
    val tiempoRestante: Int by viewModel.tiempoRestante.observeAsState(initial = 0)

    val startedExitAnimation by viewModel.startedExitAnimation.observeAsState(initial = false)
    val startedEnterAnimation by viewModel.startedEnterAnimation.observeAsState(initial = false)

    val outAnimation by animateFloatAsState(
        targetValue = if (startedExitAnimation) -90f else 0f, // Rotar -90 grados
        animationSpec = tween(durationMillis = 700, easing = EaseInQuart), label = "Splash Rotation"
    )

    val inAnimation by animateFloatAsState(
        targetValue = if (startedEnterAnimation) 0f else 90f, // Rotar 90 grados
        animationSpec = tween(durationMillis = 700, easing = EaseInQuart), label = "Splash Rotation"
    )
    val questionAlpha by animateFloatAsState(
        targetValue = if (startedEnterAnimation) 1f else 0f,
        animationSpec = tween(400, delayMillis = 0, easing = EaseInQuart),
        label = "Shield Alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),

        ) {

        ProgressArea(racha) // Contador de racha de aciertos

        // Subsección de la pregunta
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .graphicsLayer(
                    rotationZ = if (startedExitAnimation) outAnimation else inAnimation, // Animación de salida o entrada
                    transformOrigin = TransformOrigin(0.5f, 3f) // Rotación desde el centro abajo
                )
                .alpha(questionAlpha)
        ) {
            QuestionCard(pregunta.pregunta)
            TimerProgressBar(tiempoRestante, pregunta.tiempo)
            pregunta.respuestas.forEach { respuesta ->
                AnswerCard(respuesta, answerSelected = {
                    viewModel.responderPregunta(respuesta)
                })
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressArea(racha: Int) {

    val progress = racha.toFloat()

    val cara = when(racha){
            in 0..3 -> R.drawable.satisfecho
            in 3..7 -> R.drawable.enfadado
            in 7..10 -> R.drawable.muyenfadado
            else -> R.drawable.muyenfadado
        }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {


        // Slider sin colores para que la pista personalizada sea visible
        Slider(
            value = progress,
            onValueChange = {},
            valueRange = 0f..10f,
            steps = 11,
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent, // Color del "thumb" del slider
                activeTrackColor = Color.Transparent, // Hacer transparente el track activo
                inactiveTrackColor = Color.Transparent, // Hacer transparente el track inactivo
            ),
            track = { DrawTrack(purpleRedGradient()) }, // Personalizar el "thumb" del slider
            thumb = {}, // No mostrar el "thumb" por completo
            modifier = Modifier
                .fillMaxWidth(0.85f)
        )

        Image(
            painter = painterResource(id = R.drawable.barra_progreso), // Reemplaza con tu recurso de imagen
            contentDescription = "Timón de madera",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(0.85f)
            // Tamaño del timón
        )

        Slider(
            value = progress,
            onValueChange = {},
            valueRange = 0f..10f,
            steps = 11, // Para representar correctamente los 360 tonos
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent, // Color del "thumb" del slider
                activeTrackColor = Color.Transparent, // Hacer transparente el track activo
                inactiveTrackColor = Color.Transparent, // Hacer transparente el track inactivo
            ),
            thumb = {
                DrawThumb(idCara = cara)
            }, // Personalizar el "thumb" del slider
            modifier = Modifier
                .fillMaxWidth(0.85f)
        )
    }
}

@Composable
fun DrawThumb(idCara : Int) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        // Imagen que actúa como botón
        Image(
            painter = painterResource(id = R.drawable.escudo), // Reemplaza con tu recurso de imagen
            contentDescription = "Timón de madera",
            modifier = Modifier.size(80.dp) // Tamaño del botón
        )

        Image(
            painter = painterResource(id = idCara), // Reemplaza con tu recurso de imagen
            contentDescription = "Escudo de madera",
            modifier = Modifier.size(45.dp) // Tamaño del botón
        )
    }
}

@Composable
fun DrawTrack(brush: Brush) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 4.dp) // Ajustar la posición
            .height(16.dp) // Ajustar el tamaño
            .drawWithCache {
                // Crear un degradado multicolor horizontal
                onDrawBehind {
                    // Dibujar la pista del slider como una barra multicolor
                    drawRoundRect(
                        brush = brush,
                        size = Size(size.width, 16.dp.toPx()), // Ajustar el grosor de la pista
                    )
                }
            }
    ) {}
}

fun purpleRedGradient(): Brush {
    return Brush.horizontalGradient(
        colors = listOf(
            Color.hsv(300f, 1f, 0.86f),
            Color.hsv(0f, 1f, 0.95f)
        )
    )
}

@Composable
fun QuestionCard(question: String) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(200.dp)
            .border(
                BorderStroke(
                    3.dp, gradienteIridiscenteSaturado()
                ), shape = RoundedCornerShape(16.dp) // Esquinas redondeadas
            )
            .graphicsLayer { // Manejo de la sombra y elevación en Material 3
                shadowElevation = 4.dp.toPx() // Sombra
                shape = RoundedCornerShape(16.dp) // Esquinas redondeadas

            },
        shape = MaterialTheme.shapes.large,
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradienteIridiscenteClaro()),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = question,
                fontFamily = PinkChicken,
                overflow = TextOverflow.Ellipsis,
                fontSize = 30.sp,
                lineHeight = 30.sp,
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun TimerProgressBar(tiempoRestante: Int, tiempoTotal: Int) {

    val progress = tiempoRestante.toFloat() / tiempoTotal.toFloat()

    Row(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(50.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        LinearProgressIndicator(
            progress = { progress },
            trackColor = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(10.dp)
                .rotate(180f)
                .offset(x = 20.dp),
            color = Color.White,
        )

        Text(
            text = "$tiempoRestante",
            fontSize = 30.sp,
            fontFamily = YouBlockhead,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )

    }
}

@Composable
fun AnswerCard(answer: String, answerSelected: () -> Unit = {}) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.80f)
            .height(60.dp)
            .border(
                BorderStroke(
                    3.dp, gradienteIridiscenteSaturado()
                ), shape = RoundedCornerShape(16.dp) // Esquinas redondeadas
            )
            .graphicsLayer { // Manejo de la sombra y elevación en Material 3
                shadowElevation = 4.dp.toPx() // Sombra
                shape = RoundedCornerShape(16.dp) // Esquinas redondeadas
            }
            .clickable {
                answerSelected()
            },
        shape = MaterialTheme.shapes.large,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradienteIridiscenteClaro()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = answer,
                fontFamily = PinkChicken,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
            )
        }
    }
}
