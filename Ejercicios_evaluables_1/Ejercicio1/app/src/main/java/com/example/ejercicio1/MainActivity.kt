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

        val inputEnlace = findViewById<EditText>(R.id.input)
        val boton = findViewById<Button>(R.id.boton)

        boton.setOnClickListener {
            val url = inputEnlace.text.toString()

            if (url.isEmpty()) {
                Toast.makeText(this, "Introduce una URL", Toast.LENGTH_LONG).show()
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                Toast.makeText(this, "Introduce una URL válida", Toast.LENGTH_LONG).show()
            } else {
                val busqueda = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(busqueda)
            }
        }
    }
}