package com.example.ejercicio8

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("RtlHardcoded", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner = findViewById<Spinner>(R.id.spinner_colores)
        val textoColor = findViewById<TextView>(R.id.texto_color)

        val bIzq = findViewById<Button>(R.id.alinearIzq)
        val bCntr = findViewById<Button>(R.id.alinearCentro)
        val bDer = findViewById<Button>(R.id.alinearDech)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.colores,
            android.R.layout.simple_spinner_item
        )

        val frase = findViewById<EditText>(R.id.frase)
        val botonFrase = findViewById<Button>(R.id.cambioFrase)
        val nuevaFrase = findViewById<TextView>(R.id.nuevaFrase)

        val anagrama1 = findViewById<EditText>(R.id.anagrama1)
        val anagrama2 = findViewById<EditText>(R.id.anagrama2)
        val botonCompr = findViewById<Button>(R.id.booleanAnag)
        val verifAnagr = findViewById<TextView>(R.id.anagrama)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val colorSeleccionado = when (position) {
                    0 -> R.color.rojo
                    1 -> R.color.verde
                    2 -> R.color.azul
                    else -> R.color.rojo
                }
                textoColor.setBackgroundColor(resources.getColor(colorSeleccionado))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        bIzq.setOnClickListener {
            textoColor.gravity = Gravity.LEFT
        }

        bCntr.setOnClickListener {
            textoColor.gravity = Gravity.CENTER
        }

        bDer.setOnClickListener {
            textoColor.gravity = Gravity.RIGHT
        }

        botonFrase.setOnClickListener {
            nuevaFrase.text = frase.text.reversed().substring(0, 1).uppercase() + frase.text.reversed().substring(1, frase.text.length).lowercase()
        }

        botonCompr.setOnClickListener {
            val palabra1 = anagrama1.text.toString()
            val palabra2 = anagrama2.text.toString()

            if (palabra1.length != palabra2.length) {
                verifAnagr.text = "No son anagramas"
            } else {

                val chars1 = palabra1.lowercase().toCharArray()
                val chars2 = palabra2.lowercase().toCharArray()

                chars1.sort()
                chars2.sort()

                if (chars1.contentEquals(chars2)) {
                    verifAnagr.text = "Son anagramas"
                } else {
                    verifAnagr.text = "No son anagramas"
                }
            }
        }

    }
}