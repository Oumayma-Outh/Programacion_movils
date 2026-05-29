package com.example.app_chats.Utilidades

import android.content.Context
import android.media.SoundPool
import android.os.Build
import com.example.app_chats.R

/**
 * SoundManager - Reproductor de efectos de sonido
 * Soporta sonidos para eventos como: mensaje recibido, mensaje enviado, etc.
 */
object SoundManager {

    private var soundPool: SoundPool? = null
    private var soundIds = mutableMapOf<String, Int>()
    private var sonidosDescargados = false

    /**
     * Inicializar SoundPool
     */
    fun initialize(context: Context) {
        if (soundPool == null) {
            soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SoundPool.Builder().setMaxStreams(4).build()
            } else {
                @Suppress("DEPRECATION")
                SoundPool(4, android.media.AudioManager.STREAM_MUSIC, 0)
            }

            // Cargar sonidos
            cargarSonidos(context)
        }
    }

    /**
     * Cargar todos los efectos de sonido
     */
    private fun cargarSonidos(context: Context) {
        try {
            // Sonido para nuevo mensaje (puede usar un sonido del sistema o uno personalizado)
            // Por ahora usaremos el sonido de notificación del sistema
            sonidosDescargados = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Reproducir sonido de nuevo mensaje
     */
    fun reproducirSonidoNuevoMensaje() {
        if (PreferencesManager.areSoundNotificationsEnabled() && sonidosDescargados) {
            try {
                val ringtone = android.media.RingtoneManager.getRingtone(
                    android.app.Application().applicationContext,
                    android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)
                )
                ringtone.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Reproducir sonido de mensaje enviado
     */
    fun reproducirSonidoMensajeEnviado() {
        if (PreferencesManager.areSoundNotificationsEnabled() && sonidosDescargados) {
            try {
                val tono = android.media.RingtoneManager.getRingtone(
                    android.app.Application().applicationContext,
                    android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_RINGTONE)
                )
                tono.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Reproducir sonido personalizado por ID
     */
    fun reproducirSonido(nombreSonido: String) {
        if (sonidosDescargados && soundPool != null) {
            val soundId = soundIds[nombreSonido] ?: return
            soundPool?.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
        }
    }

    /**
     * Detener todos los sonidos
     */
    fun detenerSonidos() {
        soundPool?.release()
        soundPool = null
        soundIds.clear()
        sonidosDescargados = false
    }

    /**
     * Reproducir vibración (si está habilitada)
     */
    fun reproducirVibracion(context: Context, duracion: Long = 200) {
        if (PreferencesManager.isVibrationEnabled()) {
            try {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = android.os.VibrationEffect.createOneShot(duracion, android.os.VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(effect)
                } else {
                    vibrator.vibrate(duracion)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Reproducir patrón de vibración
     */
    fun reproducirPatronVibracion(context: Context, patron: LongArray) {
        if (PreferencesManager.isVibrationEnabled()) {
            try {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = android.os.VibrationEffect.createWaveform(patron, -1)
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(patron, -1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
