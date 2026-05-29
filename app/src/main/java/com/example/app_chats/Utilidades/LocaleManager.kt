package com.example.app_chats.Utilidades

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * LocaleManager - Gestionar cambios de idioma dinámicamente
 */
object LocaleManager {

    private const val TAG = "LocaleManager"

    /**
     * Cambiar el idioma de la app
     * @param context contexto de la app
     * @param languageCode código del idioma (es, en, fr)
     */
    fun setLocale(context: Context, languageCode: String) {
        val locale = when (languageCode) {
            "en" -> Locale("en")
            "fr" -> Locale("fr")
            else -> Locale("es")
        }

        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    /**
     * Obtener el Locale actual
     */
    fun getCurrentLocale(context: Context): String {
        return context.resources.configuration.locales[0].language
    }

    /**
     * Aplicar idioma guardado al iniciar la app
     */
    fun applyStoredLanguage(context: Context) {
        val language = PreferencesManager.getLanguage()
        setLocale(context, language)
    }
}
