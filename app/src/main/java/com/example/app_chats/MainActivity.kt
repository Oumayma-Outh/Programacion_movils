package com.example.app_chats

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.app_chats.Fragmentos.FragmentoChats
import com.example.app_chats.Fragmentos.FragmentoUsuarios
import com.example.app_chats.Modelo.Usuario
import com.example.app_chats.Perfil.PerfilActivity
import com.example.app_chats.Utilidades.CloudinaryManager
import com.example.app_chats.Utilidades.PreferencesManager
import com.example.app_chats.Utilidades.AppNotificationManager
import com.example.app_chats.Utilidades.SoundManager
import com.example.app_chats.Utilidades.LocaleManager
import com.example.app_chats.Utilidades.ThemeManager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {


    var reference : DatabaseReference? = null
    var firebaseUser : FirebaseUser? = null

    private lateinit var nombre_usuario : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar PreferencesManager para obtener preferencias guardadas
        PreferencesManager.initialize(this)
        
        // Aplicar tema guardado
        ThemeManager.applyStoredTheme(this)
        
        // Aplicar idioma guardado
        LocaleManager.applyStoredLanguage(this)
        
        setContentView(R.layout.activity_main)
        
        // Inicializar todos los managers
        CloudinaryManager.initialize(this)
        AppNotificationManager.crearCanalNotificacion(this)
        SoundManager.initialize(this)
        
        InicializarComponentes()
        ObtenerDato()

    }

    fun InicializarComponentes(){


        val toolbar : Toolbar = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""


        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(firebaseUser!!.uid)
        nombre_usuario = findViewById(R.id.Nombre_usuario)


        val tabLayout : TabLayout = findViewById(R.id.TabLayoutMain)
        val viewPager : ViewPager = findViewById(R.id.ViewPagerMain)

        val viewpagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewpagerAdapter.addItem(FragmentoUsuarios(), "Usuarios")
        viewpagerAdapter.addItem(FragmentoChats(), "Chats")

        viewPager.adapter = viewpagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    fun ObtenerDato(){

        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    val usuario : Usuario? = snapshot.getValue(Usuario::class.java)
                    nombre_usuario.text= usuario!!.getN_Usuario()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                android.util.Log.e("MainActivity", "Database error: ${error.message}")
                Toast.makeText(applicationContext, "Error al leer datos", Toast.LENGTH_SHORT).show()
            }

        })
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        private val listaFragmentos: MutableList<Fragment> = ArrayList()
        private val listaTitulos: MutableList<String> = ArrayList()

        override fun getCount(): Int {
           return listaFragmentos.size
        }

        override fun getItem(position: Int): Fragment {
           return listaFragmentos[position]
        }
        override fun getPageTitle(position: Int): CharSequence? {
            return listaTitulos[position]
        }

        fun addItem(fragment: Fragment, titulo: String) {
            listaFragmentos.add(fragment)
            listaTitulos.add(titulo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_principal,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_perfil->{
                val intent = Intent(applicationContext, PerfilActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_acerca_de -> {

                Toast.makeText(applicationContext, "Acerca de", Toast.LENGTH_SHORT).show()
                return true
            }
           R.id.menu_salir->{

               FirebaseAuth.getInstance().signOut()
               val intent = Intent(this@MainActivity, Inicio::class.java)
               Toast.makeText(applicationContext,"Has cerrado sesion", Toast.LENGTH_SHORT).show()
               startActivity(intent)
               return true

           }
            else ->  super.onOptionsItemSelected(item)

        }
    }
}