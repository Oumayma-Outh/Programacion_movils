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

        val Nombre_de_usuario : String = Registro_EditT_Nombre_Usuario.text.toString()
        val Correo_Electronico : String = R_ET_Email.text.toString()
        val Contraseña = R_ET_Codigo.text.toString()
        val Repetir_Codigo = R_ET_RepetirCode.text.toString()

/* esto es para k si el usuario no introduce dato le da de introducirlo antes de registrar */
        if(Nombre_de_usuario.isEmpty()){

            Toast.makeText(applicationContext,"Ingrese el nombre del usuario" , Toast.LENGTH_SHORT).show()

        }
        else if (Correo_Electronico.isEmpty()){
            Toast.makeText(applicationContext,"Ingrese tu correo" , Toast.LENGTH_SHORT).show()
        }
        else if (Contraseña.isEmpty()){
            Toast.makeText(applicationContext,"Ingrese tu contraseña" , Toast.LENGTH_SHORT).show()
        }
        else if (Repetir_Codigo.isEmpty()){
            Toast.makeText(applicationContext,"porfi , repite tu contraseña" , Toast.LENGTH_SHORT).show()
        }
        else if (!Contraseña.equals(Repetir_Codigo)){
            Toast.makeText(applicationContext,"Contraseña incorreta , repite otra vez " , Toast.LENGTH_SHORT).show()
        }
        else{

            RegistrarUsuario(Correo_Electronico,Contraseña)
        }
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

