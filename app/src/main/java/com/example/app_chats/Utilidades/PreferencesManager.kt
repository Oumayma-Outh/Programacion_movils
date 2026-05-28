package com.example.app_chats.Utilidades

import android.content.Context
import android.content.SharedPreferences
import com.example.app_chats.R

/**
 * PreferencesManager - Gestionar preferencias del usuario
 * Incluye: temas, notificaciones, tamaño de letra, idioma
 */
object PreferencesManager {

    private const val PREFS_NAME = "app_chats_prefs"
    private const val KEY_THEME = "theme_mode" // dark, light, auto
    private const val KEY_NOTIFICATIONS = "notifications_enabled"
    private const val KEY_FONT_SIZE = "font_size" // small, medium, large
    private const val KEY_LANGUAGE = "language" // es, en, fr
    private const val KEY_SOUND_NOTIFICATIONS = "sound_notifications"
    private const val KEY_VIBRATION = "vibration"
    private const val KEY_FIRST_TIME = "first_time"

    private lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // ========== TEMA ==========
    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME, mode).apply()
    }

    fun getThemeMode(): String {
        return prefs.getString(KEY_THEME, "auto") ?: "auto"
    }

    // ========== NOTIFICACIONES ==========
    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
    }

    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATIONS, true)
    }

    fun setSoundNotifications(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND_NOTIFICATIONS, enabled).apply()
    }

    fun areSoundNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_SOUND_NOTIFICATIONS, true)
    }

    fun setVibration(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VIBRATION, enabled).apply()
    }

    fun isVibrationEnabled(): Boolean {
        return prefs.getBoolean(KEY_VIBRATION, true)
    }

    // ========== TAMAÑO DE LETRA ==========
    fun setFontSize(size: String) {
        prefs.edit().putString(KEY_FONT_SIZE, size).apply()
    }

    fun getFontSize(): String {
        return prefs.getString(KEY_FONT_SIZE, "medium") ?: "medium"
    }

    fun getFontSizeMultiplier(): Float {
        return when (getFontSize()) {
            "small" -> 0.85f
            "large" -> 1.15f
            else -> 1.0f
        }
    }

    // ========== IDIOMA ==========
    fun setLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, "es") ?: "es"
    }

    // ========== PRIMER ACCESO ==========
    fun setFirstTimeVisit(visited: Boolean) {
        prefs.edit().putBoolean(KEY_FIRST_TIME, visited).apply()
    }

    fun isFirstTimeVisit(): Boolean {
        return prefs.getBoolean(KEY_FIRST_TIME, true)
    }

    // ========== LIMPIAR PREFERENCIAS ==========
    fun clearAllPreferences() {
        prefs.edit().clear().apply()
    }

    // ========== OBTENER TODAS LAS PREFERENCIAS ==========
    fun getAllPreferences(): Map<String, *> {
        return prefs.all
    }
}
