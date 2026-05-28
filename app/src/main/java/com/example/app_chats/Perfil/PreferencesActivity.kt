package com.example.app_chats.Perfil

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.app_chats.R
import com.example.app_chats.Utilidades.PreferencesManager

/**
 * PreferencesActivity - Configuración de preferencias del usuario
 * - Tema (claro/oscuro/automático)
 * - Notificaciones (activadas/desactivadas)
 * - Sonido de notificaciones
 * - Vibración
 * - Tamaño de letra (pequeño/medio/grande)
 * - Idioma (Español/English/Français)
 */
class PreferencesActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var spinnerTema: Spinner
    private lateinit var spinnerTamanoLetra: Spinner
    private lateinit var spinnerIdioma: Spinner
    private lateinit var switchNotificaciones: Switch
    private lateinit var switchSonido: Switch
    private lateinit var switchVibracion: Switch
    private lateinit var btnGuardarPreferencias: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        // Inicializar PreferencesManager
        PreferencesManager.initialize(this)

        InicializarComponentes()
        ConfigurarSpinners()
        CargarPreferenciasActuales()
        ConfigurarListeners()
    }

    private fun InicializarComponentes() {
        toolbar = findViewById(R.id.toolbarPreferences)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Preferencias"

        spinnerTema = findViewById(R.id.spinnerTema)
        spinnerTamanoLetra = findViewById(R.id.spinnerTamanoLetra)
        spinnerIdioma = findViewById(R.id.spinnerIdioma)
        switchNotificaciones = findViewById(R.id.switchNotificaciones)
        switchSonido = findViewById(R.id.switchSonido)
        switchVibracion = findViewById(R.id.switchVibracion)
        btnGuardarPreferencias = findViewById(R.id.btnGuardarPreferencias)
    }

    private fun ConfigurarSpinners() {
        // Spinner Tema
        val temasArray = arrayOf("Automático", "Claro", "Oscuro")
        val temasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, temasArray)
        temasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTema.adapter = temasAdapter

        // Spinner Tamaño de Letra
        val tamanoArray = arrayOf("Pequeño", "Medio", "Grande")
        val tamanoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tamanoArray)
        tamanoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTamanoLetra.adapter = tamanoAdapter

        // Spinner Idioma
        val idiomas = arrayOf("Español", "English", "Français")
        val idiomaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, idiomas)
        idiomaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIdioma.adapter = idiomaAdapter
    }

    private fun CargarPreferenciasActuales() {
        // Cargar tema
        when (PreferencesManager.getThemeMode()) {
            "light" -> spinnerTema.setSelection(1)
            "dark" -> spinnerTema.setSelection(2)
            else -> spinnerTema.setSelection(0)
        }

        // Cargar tamaño de letra
        when (PreferencesManager.getFontSize()) {
            "small" -> spinnerTamanoLetra.setSelection(0)
            "large" -> spinnerTamanoLetra.setSelection(2)
            else -> spinnerTamanoLetra.setSelection(1)
        }

        // Cargar idioma
        when (PreferencesManager.getLanguage()) {
            "en" -> spinnerIdioma.setSelection(1)
            "fr" -> spinnerIdioma.setSelection(2)
            else -> spinnerIdioma.setSelection(0)
        }

        // Cargar switches
        switchNotificaciones.isChecked = PreferencesManager.areNotificationsEnabled()
        switchSonido.isChecked = PreferencesManager.areSoundNotificationsEnabled()
        switchVibracion.isChecked = PreferencesManager.isVibrationEnabled()

        // Deshabilitar sonido si las notificaciones están desactivadas
        switchSonido.isEnabled = switchNotificaciones.isChecked
    }

    private fun ConfigurarListeners() {
        switchNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            switchSonido.isEnabled = isChecked
            if (!isChecked) {
                switchSonido.isChecked = false
            }
        }

        btnGuardarPreferencias.setOnClickListener {
            GuardarPreferencias()
        }
    }

    private fun GuardarPreferencias() {
        // Guardar tema
        val temaSeleccionado = when (spinnerTema.selectedItemPosition) {
            1 -> "light"
            2 -> "dark"
            else -> "auto"
        }
        PreferencesManager.setThemeMode(temaSeleccionado)

        // Guardar tamaño de letra
        val tamanoSeleccionado = when (spinnerTamanoLetra.selectedItemPosition) {
            0 -> "small"
            2 -> "large"
            else -> "medium"
        }
        PreferencesManager.setFontSize(tamanoSeleccionado)

        // Guardar idioma
        val idiomaSeleccionado = when (spinnerIdioma.selectedItemPosition) {
            1 -> "en"
            2 -> "fr"
            else -> "es"
        }
        PreferencesManager.setLanguage(idiomaSeleccionado)

        // Guardar switches
        PreferencesManager.setNotificationsEnabled(switchNotificaciones.isChecked)
        PreferencesManager.setSoundNotifications(switchSonido.isChecked)
        PreferencesManager.setVibration(switchVibracion.isChecked)

        Toast.makeText(this, "Preferencias guardadas correctamente", Toast.LENGTH_SHORT).show()

        // Volver atrás
        Thread {
            Thread.sleep(1000)
            finish()
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
