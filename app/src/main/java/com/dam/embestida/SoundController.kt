package com.dam.embestida

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner


fun playDemolitionSound(context: Context) {
    // Reproduce el sonido cuando el escudo termina la animación
    val mediaPlayer = MediaPlayer.create(context, R.raw.demolition)
    mediaPlayer.start()
    mediaPlayer.setOnCompletionListener {
        it.release() // Libera el recurso cuando el sonido finaliza
    }
}


@Composable
fun PlayHomeMusic(context: Context) {

    // Referencia al LifecycleOwner para observar los cambios en el ciclo de vida
    val lifecycleOwner = LocalLifecycleOwner.current
    var mediaPlayer: MediaPlayer?

    // Reproduce la musica
    DisposableEffect(lifecycleOwner) {

        mediaPlayer = MediaPlayer.create(context, R.raw.renaissance_revelry_jon_presstone)
        mediaPlayer?.isLooping = true // Configura el reproductor para repetir la música
        mediaPlayer?.start()

        // Observador del ciclo de vida para detener la música cuando la app se minimiza
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    // Pausa la música cuando la app se minimiza o va a segundo plano
                    mediaPlayer?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    // Reanuda la música cuando la app vuelve a primer plano
                    mediaPlayer?.start()
                }

                Lifecycle.Event.ON_STOP -> {
                    // Detiene la música completamente si la app se detiene
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                }

                else -> Unit
            }
        }

        // Añadir el observador al ciclo de vida
        lifecycleOwner.lifecycle.addObserver(observer)

        // Asegúrate de liberar el recurso cuando el Composable salga de pantalla
        onDispose {
            mediaPlayer?.stop() // Detiene la música cuando el usuario deja la pantalla
            mediaPlayer?.release() // Libera los recursos del MediaPlayer
            mediaPlayer = null
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun PlayGameMusic(context: Context, soundId: Int) {

    // Referencia al LifecycleOwner para observar los cambios en el ciclo de vida
    val lifecycleOwner = LocalLifecycleOwner.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Reproduce la musica
    /**
     * Esto asegura que cuando el soundId cambie,
     * el efecto se vuelva a ejecutar, deteniendo el MediaPlayer anterior
     * y creando uno nuevo con el nuevo sonido.
     *
     */
    DisposableEffect(lifecycleOwner, soundId) {
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.isLooping = true // Configura el reproductor para repetir la música
        mediaPlayer?.start()

        if(soundId == R.raw.heroic_demise)
            mediaPlayer?.seekTo(1222)

        // Observador del ciclo de vida para detener la música cuando la app se minimiza
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    // Pausa la música cuando la app se minimiza o va a segundo plano
                    mediaPlayer?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    // Reanuda la música cuando la app vuelve a primer plano
                    mediaPlayer?.start()
                }

                Lifecycle.Event.ON_STOP -> {
                    // Detiene la música completamente si la app se detiene
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                }

                else -> Unit
            }
        }

        // Añadir el observador al ciclo de vida
        lifecycleOwner.lifecycle.addObserver(observer)

        // Asegúrate de liberar el recurso cuando el Composable salga de pantalla
        onDispose {
            mediaPlayer?.stop() // Detiene la música cuando el usuario deja la pantalla
            mediaPlayer?.release() // Libera los recursos del MediaPlayer
            mediaPlayer = null
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

