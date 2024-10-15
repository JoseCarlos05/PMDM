package com.example.ejercicio1

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputEnlace = findViewById<EditText>(R.id.input)  //Creamos variables a partir de los elementos del XML
        val boton = findViewById<Button>(R.id.boton)

        boton.setOnClickListener {  //Al pulsar el botón...
            val url = inputEnlace.text.toString()  //Metemos el contenido escrito por el usuario en una variable

            if (url.isEmpty()) {  //Si se ha pulsado el botón sin haber nada escrito salta un mensaje.
                Toast.makeText(this, "Introduce una URL", Toast.LENGTH_LONG).show()
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {  //Comprueba si la URL es válida
                Toast.makeText(this, "Introduce una URL válida", Toast.LENGTH_LONG).show() //En caso de no serlo, saltará el correspondiente mensaje
            } else {
                val busqueda = Intent(Intent.ACTION_VIEW, Uri.parse(url))  //Hacemos que la variable URL se trate como tal
                startActivity(busqueda)  //Se empieza a buscar la URL
            }
        }
    }
}