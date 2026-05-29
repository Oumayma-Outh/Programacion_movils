package com.example.app_chats.Chat

import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_chats.Adaptador.AdaptadorChat
import com.example.app_chats.Modelo.Chat
import com.example.app_chats.Modelo.Usuario
import com.example.app_chats.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MensajesActivity : AppCompatActivity() {

    private lateinit var imagen_perfil_chat: ImageView
    private lateinit var N_usuario_chat: TextView
    private lateinit var Et_mensaje: EditText
    private lateinit var IB_Enviar: ImageButton
    var uid_usuario_seleccionado: String = ""

    private lateinit var IB_Adjuntar: ImageButton

    private var imagenUri: Uri? = null
    var firebaseUser: FirebaseUser? = null


    lateinit var RV_chats: RecyclerView
    var chatAdapter: AdaptadorChat? = null
    var chatLista: List<Chat>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajes)
        InicializarVistas()
        ObtenerUid()
        LeerInfoUsuarioSeleccionado()



        IB_Enviar.setOnClickListener {
            val mensaje = Et_mensaje.text.toString()
            if (mensaje.isEmpty()) {
                Toast.makeText(applicationContext, "Porfi ingresa un mensaje", Toast.LENGTH_SHORT)
                    .show()
            } else {
                EnviarMensaje(firebaseUser!!.uid, uid_usuario_seleccionado, mensaje)
                Et_mensaje.setText("")

            }

        }

    }


    private fun ObtenerUid() {

        intent = intent
        uid_usuario_seleccionado = intent.getStringExtra("uid_usuario").toString()
    }

    private fun EnviarMensaje(uid_emisor: String, uid_receptor: String, mensaje: String) {


        val reference = FirebaseDatabase.getInstance().reference
        val mensajeKey = reference.push().key

        val infoMensaje = HashMap<String, Any?>()
        infoMensaje["id_mensaje"] = mensajeKey
        infoMensaje["emisor"] = uid_emisor
        infoMensaje["receptor"] = uid_receptor
        infoMensaje["mensaje"] = mensaje
        infoMensaje["url"] = ""
        infoMensaje["visto"] = false
        reference.child("Chats").child(mensajeKey!!).setValue(infoMensaje)
            .addOnCompleteListener { tarea ->

                if (tarea.isSuccessful) {
                    val listaMensajeEmisor =
                        FirebaseDatabase.getInstance().reference.child("ListaMensajes")
                            .child(firebaseUser!!.uid)
                            .child(uid_usuario_seleccionado)

                    listaMensajeEmisor.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {

                                listaMensajeEmisor.child("uid").setValue(uid_usuario_seleccionado)
                            }


                            val listaMensajesReceptor =
                                FirebaseDatabase.getInstance().reference.child("ListaMensajes")
                                    .child(uid_usuario_seleccionado)
                                    .child(firebaseUser!!.uid)
                            listaMensajeEmisor.child("uid").setValue(firebaseUser!!.uid)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            android.util.Log.e("MensajesActivity", "Database error (EnviarMensaje listener): ${error.message}")
                            Toast.makeText(applicationContext, "Error al actualizar lista de mensajes", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }


    }


    private fun InicializarVistas() {

        Et_mensaje = findViewById(R.id.Et_mensaje)
        IB_Enviar = findViewById(R.id.IB_Enviar)
        imagen_perfil_chat = findViewById(R.id.imagen_perfil_chat)
        N_usuario_chat = findViewById(R.id.N_usuario_chat)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        IB_Adjuntar = findViewById(R.id.IB_Adjuntar)

        RV_chats = findViewById(R.id.RV_chats)
        RV_chats.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        RV_chats.layoutManager = linearLayoutManager


    }

    private fun LeerInfoUsuarioSeleccionado() {

        val reference = FirebaseDatabase.getInstance().reference.child("Usuarios")
            .child(uid_usuario_seleccionado)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario: Usuario? = snapshot.getValue(Usuario::class.java)

                //obtener el nombre del usuario

                N_usuario_chat.text = usuario!!.getN_Usuario()

                Glide.with(applicationContext).load(usuario.getImagen())
                    .placeholder(R.drawable.ic_item_usuario)
                    .into(imagen_perfil_chat)


                RecuperarMensajes(firebaseUser!!.uid, uid_usuario_seleccionado, usuario.getImagen())
            }

            override fun onCancelled(error: DatabaseError) {
                android.util.Log.e("MensajesActivity", "Database error (LeerInfoUsuario): ${error.message}")
                Toast.makeText(applicationContext, "Error al leer información del usuario", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun RecuperarMensajes(
        Emisoruid: String,
        ReceptorUid: String,
        ReceptorImagen: String?
    ) {

        chatLista = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                (chatLista as ArrayList<Chat>).clear()
                for (sn in snapshot.children) {

                    val chat = sn.getValue(Chat::class.java) /*obtenern info de cada msj*/

                    if (chat!!.getReceptor().equals(Emisoruid) && chat.getEmisor().equals(ReceptorUid)
                        || chat.getReceptor().equals(ReceptorUid) && chat.getEmisor().equals(Emisoruid)) {

                        (chatLista as ArrayList<Chat>).add(chat)
                    }


                    chatAdapter = AdaptadorChat(this@MensajesActivity, (chatLista as ArrayList<Chat>), ReceptorImagen!!)

                    RV_chats.adapter = chatAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                android.util.Log.e("MensajesActivity", "Database error (RecuperarMensajes): ${error.message}")
                Toast.makeText(applicationContext, "Error al recuperar mensajes", Toast.LENGTH_SHORT).show()
            }

        })
    }

}