package com.example.app_chats

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Inicio : AppCompatActivity() {

    private lateinit var btn_registrar : Button
    private lateinit var btn_logear : Button

    var firebaseUser : FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inicio)

        btn_registrar = findViewById(R.id.btn_registrar)
        btn_logear = findViewById(R.id.btn_logear)

      // es para cuando presionamos el button --> dirigaal usuario

      btn_registrar.setOnClickListener {

          val intent = Intent(this@Inicio, RegistroActivity::class.java)
          Toast.makeText(applicationContext, "Registros", Toast.LENGTH_SHORT).show()
          startActivity(intent)

      }

        btn_logear.setOnClickListener {

            val intent = Intent(this@Inicio, RegistroActivity::class.java)
            Toast.makeText(applicationContext, "Login", Toast.LENGTH_SHORT).show()
            startActivity(intent)

        }



    }
/*SI EL USUARIO ESTA REGISTRADO , ENTRA AL MAIN DIRCTAMENTE NO HACE FALTA REGISTRAR OTRA VEZ */
    private fun ComprobarSesion(){
                firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser != null){

                    val intent = Intent(this@Inicio , MainActivity::class.java)
                    Toast.makeText(applicationContext , "La session esta activa" , Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()


                }
    }

    override fun onStart() {
        ComprobarSesion()
        super.onStart()
    }

}
