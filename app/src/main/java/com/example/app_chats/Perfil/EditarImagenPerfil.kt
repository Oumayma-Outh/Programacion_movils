package com.example.app_chats.Perfil

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app_chats.R
import com.example.app_chats.Utilidades.CloudinaryManager

/**
 * EditarImagenPerfil - Activity para seleccionar y subir imagen de perfil a Cloudinary
 */
class EditarImagenPerfil : AppCompatActivity() {

    private lateinit var BtnElegirImagen: Button
    private lateinit var BtnActualizarImagen: Button
    private lateinit var previewImagen: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var containerProgreso: LinearLayout
    
    private var imagenSeleccionadaUri: Uri? = null
    private var progressDialog: ProgressDialog? = null

    // Registrar el contrato para elegir imagen desde galería
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagenSeleccionadaUri = uri
            // Mostrar preview de la imagen seleccionada
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.ic_item_usuario)
                .into(previewImagen)
            
            Toast.makeText(
                this,
                "Imagen seleccionada. Presiona 'Actualizar' para subir",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_imagen_perfil)

        InicializarComponentes()
        ConfigurarListeners()
    }

    private fun InicializarComponentes() {
        BtnElegirImagen = findViewById(R.id.BtnElegirImagenDe)
        BtnActualizarImagen = findViewById(R.id.BtnActualizarImagen)
        previewImagen = findViewById(R.id.previewImagen)
        progressBar = findViewById(R.id.progressBar)
        containerProgreso = findViewById(R.id.containerProgreso)
    }

    private fun ConfigurarListeners() {
        BtnElegirImagen.setOnClickListener {
            AbrirGaleria()
        }

        BtnActualizarImagen.setOnClickListener {
            if (imagenSeleccionadaUri != null) {
                SubirImagen()
            } else {
                Toast.makeText(
                    this,
                    "Debes seleccionar una imagen primero",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Abrir galería para seleccionar imagen
     */
    private fun AbrirGaleria() {
        pickImageLauncher.launch("image/*")
    }

    /**
     * Subir imagen a Cloudinary
     */
    private fun SubirImagen() {
        if (imagenSeleccionadaUri == null) return

        // Mostrar diálogo de progreso
        progressDialog = ProgressDialog(this).apply {
            setTitle("Subiendo imagen")
            setMessage("Por favor espera...")
            setCancelable(false)
            show()
        }

        BtnElegirImagen.isEnabled = false
        BtnActualizarImagen.isEnabled = false

        // Usar CloudinaryManager para subir
        CloudinaryManager.subirFotoPerfil(imagenSeleccionadaUri!!, object :
            CloudinaryManager.UploadListener {
            
            override fun onSuccess(url: String) {
                progressDialog?.dismiss()
                BtnElegirImagen.isEnabled = true
                BtnActualizarImagen.isEnabled = true
                
                Log.d("EditarImagenPerfil", "Imagen subida exitosamente: $url")
                Toast.makeText(
                    this@EditarImagenPerfil,
                    "¡Foto actualizada correctamente!",
                    Toast.LENGTH_SHORT
                ).show()
                
                // Volver a PerfilActivity después de 1 segundo
                Thread {
                    Thread.sleep(1000)
                    finish()
                }.start()
            }

            override fun onError(message: String) {
                progressDialog?.dismiss()
                BtnElegirImagen.isEnabled = true
                BtnActualizarImagen.isEnabled = true
                
                Log.e("EditarImagenPerfil", "Error: $message")
                Toast.makeText(
                    this@EditarImagenPerfil,
                    "Error: $message",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onProgress(progress: Int) {
                progressDialog?.progress = progress
                Log.d("EditarImagenPerfil", "Progreso: $progress%")
            }
        })
    }
}