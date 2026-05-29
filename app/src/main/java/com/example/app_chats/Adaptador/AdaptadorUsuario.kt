package com.example.app_chats.Adaptador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_chats.Chat.MensajesActivity
import com.example.app_chats.Modelo.Usuario
import com.example.app_chats.R

class AdaptadorUsuario (context : Context, listaUsuarios : List<Usuario> , chatLeido:Boolean)  : RecyclerView.Adapter<AdaptadorUsuario.ViewHolder?>(){
    private val context : Context
    private val listaUsuarios : List<Usuario>
    private val chatLeido : Boolean

    init {
        this.context = context
        this.listaUsuarios = listaUsuarios
        this.chatLeido = chatLeido
    }



    class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        var nombre_usuario : TextView
        var email_usuario : TextView
        var imagen_usuario : ImageView

        init {
            nombre_usuario = itemView.findViewById(R.id.Item_nombre_usuario)
            email_usuario = itemView.findViewById(R.id.Item_email_usuario)
            imagen_usuario = itemView.findViewById(R.id.Item_imagen)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view : View = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val usuario : Usuario = listaUsuarios[position]
        holder.nombre_usuario.text = usuario.getN_Usuario()
        holder.email_usuario.text = usuario.getEmail()

        // Cargar imagen con manejo de URL vacía
        val imagenUrl = usuario.getImagen()
        if (!imagenUrl.isNullOrEmpty() && imagenUrl.isNotBlank()) {
            Glide.with(context)
                .load(imagenUrl)
                .placeholder(R.drawable.ic_item_usuario)
                .error(R.drawable.ic_item_usuario)
                .into(holder.imagen_usuario)
        } else {
            // Si la URL está vacía o es nula, mostrar el placeholder
            Glide.with(context)
                .load(R.drawable.ic_item_usuario)
                .into(holder.imagen_usuario)
        }
        
        // Cuando se selecciona un usuario, abre la pantalla de mensajes
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MensajesActivity::class.java)
            intent.putExtra("uid_usuario", usuario.getUid())
            Toast.makeText(context , "El usuario selecionado es : "+usuario.getN_Usuario(), Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return listaUsuarios.size
    }



}