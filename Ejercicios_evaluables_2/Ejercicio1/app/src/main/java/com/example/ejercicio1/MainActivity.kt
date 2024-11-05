package com.example.ejercicio1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val formNombre = findViewById<EditText>(R.id.formNombre)
        val formCorreo = findViewById<EditText>(R.id.formCorreo)
        val formTelef = findViewById<EditText>(R.id.formTelef)
        val botonEnviar = findViewById<Button>(R.id.formBoton)

        botonEnviar.setOnClickListener {
            if (formNombre.text.isEmpty() || formCorreo.text.isEmpty() || formTelef.text.isEmpty()) {
                Toast.makeText(this, "Error. Hay campos vacíos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Formulario enviado con éxito", Toast.LENGTH_SHORT).show()
            }
        }
    }
}