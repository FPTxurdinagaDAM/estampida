package com.dam.embestida

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 *  ViewModel para gestionar el juego
 *
 */
class GameViewModel : ViewModel(), CoroutineScope {

    // Definir el contexto de la corrutina
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // Datos del juego
    private val _furia = MutableLiveData<Int>()
    val furia: LiveData<Int> = _furia

    // Lista de preguntas
    private val _preguntas = MutableLiveData<List<Pregunta>>()
    val preguntas: LiveData<List<Pregunta>> = _preguntas

    // Pregunta
    private val _pregunta = MutableLiveData<Pregunta>()
    val pregunta: LiveData<Pregunta> = _pregunta

    // Tiempo restante para la pregunta actual
    private val _tiempoRestante = MutableLiveData<Int>()
    val tiempoRestante: LiveData<Int> = _tiempoRestante

    // Variable para manejar si el juego está en curso
    private val _juegoEnCurso = MutableLiveData<Boolean>(false)
    val juegoEnCurso: LiveData<Boolean> = _juegoEnCurso

    // Variable para manejar si el juego está en curso
    private val _startedEnterAnimation = MutableLiveData<Boolean>(false)
    val startedEnterAnimation: LiveData<Boolean> = _startedEnterAnimation

    private val _startedExitAnimation = MutableLiveData<Boolean>(false)
    val startedExitAnimation: LiveData<Boolean> = _startedExitAnimation


    private val _musica = MutableStateFlow<Int>(R.raw.awesomeness)
    val musica: MutableStateFlow<Int> = _musica


    // Job para manejar el temporizador
    private var temporizadorJob: Job? = null

    init {
        iniciarJuego()
    }

    // Función para iniciar el juego
    fun iniciarJuego() {
        // Generamos las preguntas
        val preguntasGeneradas = generarPreguntasReales()
        _preguntas.value = preguntasGeneradas
        _furia.value = 0
        _juegoEnCurso.value = true

        launch {
            delay(700)
            siguientePregunta()
        }
    }

    // Función para avanzar a la siguiente pregunta
    fun siguientePregunta() {
        // Cancelar temporizador si el usuario responde
        temporizadorJob?.cancel()

        val listaPreguntas = _preguntas.value ?: return

        // Seleccionamos una nueva pregunta
        val nuevaPregunta = listaPreguntas.random()
        _pregunta.value = nuevaPregunta

        // Establecemos el tiempo de la pregunta
        _tiempoRestante.value = nuevaPregunta.tiempo

        _startedEnterAnimation.value = true
        _startedExitAnimation.value = false

        launch {
            delay(200)

            // Iniciamos la cuenta regresiva
            iniciarTemporizador(nuevaPregunta.tiempo)
        }
    }

    // Función para manejar la respuesta del usuario
    fun responderPregunta(respuestaSeleccionada: String) {
        val pregunta = _pregunta.value ?: return

        val preguntaAcertada = respuestaSeleccionada == pregunta.respuestaCorrecta

        // Cancelar temporizador si el usuario responde
        temporizadorJob?.cancel()

        // Actualizar la furia según la respuesta
        if (preguntaAcertada) {
            _furia.value = _furia.value?.plus(1) // Incrementa la furia
        } else {
            _furia.value = _furia.value?.plus(-1) // Decrementa la furia
        }

        // Cambiamos de canción si furioso
        if(_furia.value!! < 5){
            _musica.value = R.raw.awesomeness
        } else {
            _musica.value = R.raw.heroic_demise
        }

        // Animacion de salida y esperamos a que termine
        _startedExitAnimation.value = true
        _startedEnterAnimation.value = false

        launch {
            delay(700)

            // Avanzar a la siguiente pregunta
            siguientePregunta()
        }
    }

    // Función para iniciar el temporizador
    private fun iniciarTemporizador(tiempo: Int) {
        temporizadorJob = launch {
            for (i in tiempo downTo 0) {
                _tiempoRestante.value = i
                delay(1000L)
            }

            // Animacion de salida y esperamos a que termine
            _startedExitAnimation.value = true
            _startedEnterAnimation.value = false

            launch {
                delay(700)

                // Avanzar a la siguiente pregunta
                responderPregunta("")
            }
        }
    }

    // Función para abandonar el juego
    fun abandonarJuego() {
        _juegoEnCurso.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel() // Cancelamos la
    }
}

// Clase para representar una pregunta
data class Pregunta(
    val pregunta: String = "",
    val respuestas: List<String> = listOf(),
    val respuestaCorrecta: String = "",
    val tiempo: Int = 10
) {
    constructor() : this("Te olvidaste de tu pregunta?", listOf("Si", "Si"), "Si", 10)
}

fun generarPreguntasReales(): List<Pregunta> {
    // Lista de países y sus capitales
    val paisesYCapitales = listOf(
        "España" to "Madrid",
        "Francia" to "París",
        "Alemania" to "Berlín",
        "Italia" to "Roma",
        "Reino Unido" to "Londres",
        "Portugal" to "Lisboa",
        "Brasil" to "Brasilia",
        "Argentina" to "Buenos Aires",
        "Japón" to "Tokio",
        "China" to "Pekín",
        "México" to "Ciudad de México",
        "Canadá" to "Ottawa",
        "Estados Unidos" to "Washington D.C.",
        "Australia" to "Canberra",
        "India" to "Nueva Delhi",
        "Rusia" to "Moscú",
        "Egipto" to "El Cairo",
        "Sudáfrica" to "Pretoria",
        "Suecia" to "Estocolmo",
        "Noruega" to "Oslo"
    )

    val listaPreguntas = mutableListOf<Pregunta>()

    // Generar 100 preguntas realistas
    for (i in 1..100) {
        // Selecciona un país y su capital
        val (pais, capitalCorrecta) = paisesYCapitales.random()

        // Selecciona otras dos capitales incorrectas al azar
        val respuestasIncorrectas = paisesYCapitales
            .filter { it.second != capitalCorrecta }
            .shuffled()
            .take(2)
            .map { it.second }

        // Crear la lista de respuestas (3 incorrectas y 1 correcta)
        val respuestas = (respuestasIncorrectas + capitalCorrecta).shuffled()

        val preguntaTexto = "¿Cuál es la capital de $pais?"

        // Crear la pregunta con sus respuestas
        val pregunta = Pregunta(
            pregunta = preguntaTexto,
            respuestas = respuestas,
            respuestaCorrecta = capitalCorrecta,
            tiempo = 10
        )

        listaPreguntas.add(pregunta)
    }

    return listaPreguntas
}