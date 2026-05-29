package com.example.app_chats.Utilidades

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.app_chats.R

/**
 * ThemeManager - Gestionar cambios de tema dinámicamente
 */
object ThemeManager {

    private const val TAG = "ThemeManager"

    /**
     * Aplicar el tema guardado
     */
    fun applyStoredTheme(context: Context) {
        val theme = PreferencesManager.getThemeMode()
        applyTheme(theme)
    }

    /**
     * Cambiar el tema
     * @param mode: "auto", "light", "dark"
     */
    fun applyTheme(mode: String) {
        when (mode) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "auto" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    /**
     * Recrear activity para aplicar tema
     */
    fun recreateActivityWithTheme(activity: Activity) {
        activity.recreate()
    }
}
