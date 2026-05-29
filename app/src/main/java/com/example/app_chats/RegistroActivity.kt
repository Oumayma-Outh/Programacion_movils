package com.example.app_chats

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistroActivity : AppCompatActivity() {


    private lateinit var  Registro_EditT_Nombre_Usuario: EditText
    private lateinit var  R_ET_Email: EditText
    private lateinit var  R_ET_Codigo: EditText
    private lateinit var  R_ET_RepetirCode: EditText
    private lateinit var btn_registr: Button

    private lateinit var auth : FirebaseAuth
    private lateinit var reference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
       // supportActionBar!!.title = "Registros"

        IniciarVariables()


        btn_registr.setOnClickListener {
                ValidarDatos()

        }
    }

    private fun IniciarVariables ( ){

        Registro_EditT_Nombre_Usuario = findViewById(R.id.Registro_EditT_Nombre_Usuario)
        R_ET_Email = findViewById(R.id.R_ET_Email)
        R_ET_Codigo=findViewById(R.id.R_ET_Codigo)
        R_ET_RepetirCode=findViewById(R.id.R_ET_RepetirCode)
        btn_registr=findViewById(R.id.btn_registr)
        auth = FirebaseAuth.getInstance()


    }

    private fun ValidarDatos ( ){

        val Nombre_de_usuario : String = Registro_EditT_Nombre_Usuario.text.toString().trim()
        val Correo_Electronico : String = R_ET_Email.text.toString().trim()
        val Contraseña = R_ET_Codigo.text.toString().trim()
        val Repetir_Codigo = R_ET_RepetirCode.text.toString().trim()

        // Validación de nombre de usuario
        if(Nombre_de_usuario.isEmpty()){
            Toast.makeText(applicationContext,"Ingrese el nombre del usuario" , Toast.LENGTH_SHORT).show()
            Registro_EditT_Nombre_Usuario.requestFocus()
            return
        }
        if (Nombre_de_usuario.length < 3) {
            Toast.makeText(applicationContext,"El nombre debe tener al menos 3 caracteres" , Toast.LENGTH_SHORT).show()
            Registro_EditT_Nombre_Usuario.requestFocus()
            return
        }
        
        // Validación de correo
        if (Correo_Electronico.isEmpty()){
            Toast.makeText(applicationContext,"Ingrese tu correo" , Toast.LENGTH_SHORT).show()
            R_ET_Email.requestFocus()
            return
        }
        if (!isValidEmail(Correo_Electronico)) {
            Toast.makeText(applicationContext,"Ingrese un correo válido" , Toast.LENGTH_SHORT).show()
            R_ET_Email.requestFocus()
            return
        }
        
        // Validación de contraseña
        if (Contraseña.isEmpty()){
            Toast.makeText(applicationContext,"Ingrese tu contraseña" , Toast.LENGTH_SHORT).show()
            R_ET_Codigo.requestFocus()
            return
        }
        if (Contraseña.length < 6) {
            Toast.makeText(applicationContext,"La contraseña debe tener al menos 6 caracteres" , Toast.LENGTH_SHORT).show()
            R_ET_Codigo.requestFocus()
            return
        }
        
        // Validación de confirmación
        if (Repetir_Codigo.isEmpty()){
            Toast.makeText(applicationContext,"Repite tu contraseña" , Toast.LENGTH_SHORT).show()
            R_ET_RepetirCode.requestFocus()
            return
        }
        
        // Validación de coincidencia de contraseñas
        if (!Contraseña.equals(Repetir_Codigo)){
            Toast.makeText(applicationContext,"Las contraseñas no coinciden" , Toast.LENGTH_SHORT).show()
            R_ET_RepetirCode.requestFocus()
            return
        }

        RegistrarUsuario(Correo_Electronico, Contraseña)
    }
    
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    private fun RegistrarUsuario(correoElectronico: String, contraseña: String) {
        auth.createUserWithEmailAndPassword(correoElectronico, contraseña)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {


                    /*uid : es el unico codigo de cada usuario */
                    var uid: String = ""
                    uid =
                        auth.currentUser!!.uid /*aqui vamos a almacen en uid al usuario actual y extrar uid: k es el codigo unico de cada usaurio */
                    reference =
                        FirebaseDatabase.getInstance().reference.child("Usuarios").child(uid)


                    val hashMap = HashMap<String, Any>()
                    val h_nombre_usuario: String = Registro_EditT_Nombre_Usuario.text.toString()
                    val h_correo: String = R_ET_Email.text.toString()


                    hashMap["uid"] = uid
                    hashMap["n_usuario"] = h_nombre_usuario
                    hashMap["correo"] = h_correo
                    hashMap["imagen"] = ""
                    hashMap["buscar"] = h_nombre_usuario.lowercase()


                    /*NUEVOS DATOS DE USUARIO */

                    hashMap["nombres"] = ""
                    hashMap["apellidos"] = ""
                    hashMap["edad"] = ""
                    hashMap["profesion"] = ""
                    hashMap["domicilio"] = ""
                    hashMap["telefono"] = ""
                    hashMap["estado"] = "offline"


                    reference.updateChildren(hashMap).addOnCompleteListener { task2 ->


                        if (task2.isSuccessful) {


                            val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                            Toast.makeText(
                                applicationContext, "Se ha registrado con exito",
                                Toast.LENGTH_SHORT
                            ).show()


                            startActivity(intent)
                        }


                    }.addOnFailureListener { e ->
                        Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT)
                            .show()


                    }


                } else {
                    Toast.makeText(applicationContext, "Ha occurido un error", Toast.LENGTH_SHORT)
                        .show()


                }


            }.addOnFailureListener { e ->


                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()


            }

    }

}

