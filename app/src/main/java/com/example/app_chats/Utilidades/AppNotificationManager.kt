package com.example.app_chats.Utilidades

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.app_chats.MainActivity
import com.example.app_chats.R

/**
 * NotificationManager - Gestionar notificaciones locales
 * Compatible con Android 8.0+ (API 26+)
 */
object AppNotificationManager {

    private const val CHANNEL_ID = "app_chats_notifications"
    private const val CHANNEL_NAME = "App Chats Notifications"
    private const val CHANNEL_DESCRIPTION = "Notificaciones de App Chats"
    private const val NOTIFICATION_ID = 1

    /**
     * Crear canal de notificación (requerido en Android 8.0+)
     */
    fun crearCanalNotificacion(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(PreferencesManager.isVibrationEnabled())
                setSound(
                    android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION),
                    android.media.AudioAttributes.Builder()
                        .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    /**
     * Enviar notificación de nuevo mensaje
     */
    fun notificarNuevoMensaje(
        context: Context,
        nombreUsuario: String,
        mensaje: String,
        usuarioId: String
    ) {
        if (!PreferencesManager.areNotificationsEnabled()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("usuario_id", usuarioId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            usuarioId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_item_usuario)
            .setContentTitle(nombreUsuario)
            .setContentText(mensaje)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (PreferencesManager.areSoundNotificationsEnabled()) {
            builder.setSound(
                android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)
            )
        }

        if (PreferencesManager.isVibrationEnabled()) {
            builder.setVibrate(longArrayOf(0, 250, 250, 250))
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(usuarioId.hashCode(), builder.build())
    }

    /**
     * Enviar notificación de evento general
     */
    fun notificarEvento(
        context: Context,
        titulo: String,
        mensaje: String,
        notificationId: Int = NOTIFICATION_ID
    ) {
        if (!PreferencesManager.areNotificationsEnabled()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_item_usuario)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (PreferencesManager.areSoundNotificationsEnabled()) {
            builder.setSound(
                android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)
            )
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    /**
     * Cancelar notificación
     */
    fun cancelarNotificacion(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    /**
     * Cancelar todas las notificaciones
     */
    fun cancelarTodas(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
}
