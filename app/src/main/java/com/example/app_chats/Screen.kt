package com.example.app_chats

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_screen)
        MostrarBienvenido()

    }

    fun MostrarBienvenido() {

        object : CountDownTimer(3000, 1080){
            override fun onTick (pb: Long) {
               // TODO("Not yet implemented")
            }

            override fun onFinish () {
                val intent = Intent(applicationContext, Inicio::class.java)
                startActivity(intent)
                finish()

            }


        }.start()


    }

}