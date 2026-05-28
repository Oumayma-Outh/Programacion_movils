package com.example.app_chats.Utilidades

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.Calendar

/**
 * DialogManager - Gestionar diferentes tipos de diálogos
 * - Alert Dialog
 * - Confirmation Dialog
 * - Date Picker
 * - Time Picker
 * - Custom Dialog
 */
object DialogManager {

    /**
     * Mostrar diálogo de alerta simple
     */
    fun mostrarAlertaSimple(
        context: Context,
        titulo: String,
        mensaje: String,
        botonPositivo: String = "Aceptar",
        accionPositiva: () -> Unit = {}
    ) {
        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton(botonPositivo) { dialog, _ ->
                accionPositiva()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Mostrar diálogo de confirmación (Sí/No)
     */
    fun mostrarConfirmacion(
        context: Context,
        titulo: String,
        mensaje: String,
        botonSi: String = "Sí",
        botonNo: String = "No",
        accionSi: () -> Unit,
        accionNo: () -> Unit = {}
    ) {
        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton(botonSi) { dialog, _ ->
                accionSi()
                dialog.dismiss()
            }
            .setNegativeButton(botonNo) { dialog, _ ->
                accionNo()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Mostrar diálogo con opciones (lista)
     */
    fun mostrarListaOpciones(
        context: Context,
        titulo: String,
        opciones: Array<String>,
        accionSeleccionada: (Int, String) -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setItems(opciones) { _, which ->
                accionSeleccionada(which, opciones[which])
            }
            .setCancelable(true)
            .show()
    }

    /**
     * Mostrar DatePicker (selector de fecha)
     */
    fun mostrarSelectorFecha(
        context: Context,
        titulo: String = "Selecciona una fecha",
        accionFechaSeleccionada: (dia: Int, mes: Int, año: Int) -> Unit
    ) {
        val calendario = Calendar.getInstance()
        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                accionFechaSeleccionada(selectedDay, selectedMonth + 1, selectedYear)
            },
            año,
            mes,
            dia
        ).show()
    }

    /**
     * Mostrar TimePicker (selector de hora)
     */
    fun mostrarSelectorHora(
        context: Context,
        titulo: String = "Selecciona una hora",
        accionHoraSeleccionada: (hora: Int, minuto: Int) -> Unit
    ) {
        val calendario = Calendar.getInstance()
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minuto = calendario.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                accionHoraSeleccionada(selectedHour, selectedMinute)
            },
            hora,
            minuto,
            true
        ).show()
    }

    /**
     * Mostrar diálogo de carga/espera (indeterminado)
     */
    fun mostrarCargando(
        context: Context,
        mensaje: String = "Cargando..."
    ): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle("Por favor espera")
            .setMessage(mensaje)
            .setCancelable(false)
            .show()
    }

    /**
     * Mostrar diálogo de error
     */
    fun mostrarError(
        context: Context,
        titulo: String = "Error",
        mensaje: String,
        botonAceptar: String = "Aceptar",
        accion: () -> Unit = {}
    ) {
        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(botonAceptar) { dialog, _ ->
                accion()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Mostrar diálogo de éxito
     */
    fun mostrarExito(
        context: Context,
        titulo: String = "Éxito",
        mensaje: String,
        botonAceptar: String = "Aceptar",
        accion: () -> Unit = {}
    ) {
        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton(botonAceptar) { dialog, _ ->
                accion()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}
