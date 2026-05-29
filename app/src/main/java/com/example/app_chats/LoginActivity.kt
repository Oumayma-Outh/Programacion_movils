package com.example.app_chats

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

class LoginActivity : AppCompatActivity() {


    private lateinit var L_Et_email : EditText
    private lateinit var L_Et_password  : EditText
    private lateinit var Btn_login : Button
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
       // supportActionBar!!.title = "Login"
        InicializarVariables()

        Btn_login.setOnClickListener {
            ValidarDatos()

        }

    }


    private fun InicializarVariables(){
        L_Et_email = findViewById(R.id.L_Et_email)
        L_Et_password = findViewById(R.id.L_Et_password)
        Btn_login = findViewById(R.id.Btn_login)
        auth = FirebaseAuth.getInstance()

    }

    private fun ValidarDatos() {
        val email : String = L_Et_email.text.toString().trim()
        val password : String = L_Et_password.text.toString().trim()

        if (email.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
            L_Et_email.requestFocus()
            return
        }
        if (!isValidEmail(email)) {
            Toast.makeText(applicationContext, "Ingrese un correo válido", Toast.LENGTH_SHORT).show()
            L_Et_email.requestFocus()
            return
        }
        if (password.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
            L_Et_password.requestFocus()
            return
        }
        if (password.length < 6) {
            Toast.makeText(applicationContext, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            L_Et_password.requestFocus()
            return
        }
        
        LoginUsuario(email, password)
    }
    
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
    private fun LoginUsuario(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    Toast.makeText(applicationContext, "Ha iniciado sesión", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e->
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}



