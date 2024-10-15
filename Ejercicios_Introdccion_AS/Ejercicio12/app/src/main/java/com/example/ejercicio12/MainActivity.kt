package com.example.ejercicio12

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonIncrement = findViewById<Button>(R.id.incrementar)
        val botonReinicar = findViewById<Button>(R.id.reiniciar)
        val textResul = findViewById<TextView>(R.id.resultado)

        val sharedPreferences: SharedPreferences = getSharedPreferences("resultado", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        textResul.text = sharedPreferences.getString("ultimoResultado", "0")

        botonIncrement.setOnClickListener {
            textResul.text = (textResul.text.toString().toInt() + 1).toString()
            editor.putString("ultimoResultado", textResul.text.toString())
            editor.apply()
        }

        botonReinicar.setOnClickListener {
            textResul.text = getString(R.string.resulInicio)
            editor.putString("ultimoResultado", textResul.text.toString())
            editor.apply()
        }
    }
}