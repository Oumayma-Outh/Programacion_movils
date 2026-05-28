package com.example.app_chats.Utilidades

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * CloudinaryManager - Helper para manejar carga de imágenes a Cloudinary
 * Cloud Name: dblb0kl2q
 * Folder Mode: Dynamic folders
 */
object CloudinaryManager {

    private const val TAG = "CloudinaryManager"
    private const val CLOUD_NAME = "dblb0kl2q"
    private const val API_KEY = "823769889636385" // Obtenido del dashboard Cloudinary
    
    // Callback para notificar el progreso de la carga
    interface UploadListener {
        fun onSuccess(url: String)
        fun onError(message: String)
        fun onProgress(progress: Int)
    }

    /**
     * Inicializar Cloudinary (llamar una sola vez en la Application o MainActivity)
     */
    fun initialize(context: Context) {
        try {
            val config = mapOf(
                "cloud_name" to CLOUD_NAME,
                "api_key" to API_KEY
            )
            MediaManager.init(context, config)
            Log.d(TAG, "Cloudinary inicializado correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error inicializando Cloudinary: ${e.message}")
        }
    }

    /**
     * Subir imagen de perfil de usuario a Cloudinary
     * @param uri - URI de la imagen
     * @param listener - Callback para escuchar el resultado
     */
    fun subirFotoPerfil(uri: Uri, listener: UploadListener) {
        try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                listener.onError("Usuario no autenticado")
                return
            }

            MediaManager.get().upload(uri)
                .option("public_id", "app_chats/profile_images/$userId")
                .option("resource_type", "auto")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        Log.d(TAG, "Iniciando carga de imagen")
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        try {
                            val url = resultData["secure_url"] as String
                            Log.d(TAG, "Imagen subida exitosamente: $url")
                            
                            // Guardar URL en Firebase
                            guardarUrlEnFirebase(userId, url, listener)
                        } catch (e: Exception) {
                            listener.onError("Error procesando respuesta: ${e.message}")
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        val errorMsg = "Error en Cloudinary: ${error.description}"
                        Log.e(TAG, errorMsg)
                        listener.onError(errorMsg)
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        val progress = (bytes * 100 / totalBytes).toInt()
                        Log.d(TAG, "Progreso de carga: $progress%")
                        listener.onProgress(progress)
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        Log.w(TAG, "Reintentando carga: ${error.description}")
                    }
                })
                .dispatch()
        } catch (e: Exception) {
            listener.onError("Error iniciando carga: ${e.message}")
            Log.e(TAG, "Exception en subirFotoPerfil", e)
        }
    }

    /**
     * Guardar URL de la imagen en Firebase Database
     */
    private fun guardarUrlEnFirebase(userId: String, url: String, listener: UploadListener) {
        try {
            val db = FirebaseDatabase.getInstance().reference
            
            db.child("Usuarios").child(userId).child("imagen")
                .setValue(url)
                .addOnSuccessListener {
                    Log.d(TAG, "URL guardada en Firebase correctamente")
                    listener.onSuccess(url)
                }
                .addOnFailureListener { e ->
                    val errorMsg = "Error guardando en Firebase: ${e.message}"
                    Log.e(TAG, errorMsg)
                    listener.onError(errorMsg)
                }
        } catch (e: Exception) {
            listener.onError("Error en guardarUrlEnFirebase: ${e.message}")
            Log.e(TAG, "Exception en guardarUrlEnFirebase", e)
        }
    }
}
