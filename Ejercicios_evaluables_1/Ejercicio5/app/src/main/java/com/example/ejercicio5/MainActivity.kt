package com.example.ejercicio5

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val resultado = findViewById<TextView>(R.id.resultado)

        val botonReset = findViewById<Button>(R.id.botonReset)

        val num0 = findViewById<Button>(R.id.boton0)
        val num1 = findViewById<Button>(R.id.boton1)
        val num2 = findViewById<Button>(R.id.boton2)
        val num3 = findViewById<Button>(R.id.boton3)
        val num4 = findViewById<Button>(R.id.boton4)
        val num5 = findViewById<Button>(R.id.boton5)
        val num6 = findViewById<Button>(R.id.boton6)
        val num7 = findViewById<Button>(R.id.boton7)
        val num8 = findViewById<Button>(R.id.boton8)
        val num9 = findViewById<Button>(R.id.boton9)

        val botonSum = findViewById<Button>(R.id.botonSum)
        val botonResta = findViewById<Button>(R.id.botonResta)
        val botonMult = findViewById<Button>(R.id.botonMult)
        val botonDiv = findViewById<Button>(R.id.botonDiv)
        val botonIgual = findViewById<Button>(R.id.botonIgual)

        botonReset.setOnClickListener {
            resultado.text = ""
        }

        num0.setOnClickListener {
            resultado.text = resultado.text.toString() + "0"
        }

        num1.setOnClickListener {
            resultado.text = resultado.text.toString() + "1"
        }

        num2.setOnClickListener {
            resultado.text = resultado.text.toString() + "2"
        }

        num3.setOnClickListener {
            resultado.text = resultado.text.toString() + "3"
        }

        num4.setOnClickListener {
            resultado.text = resultado.text.toString() + "4"
        }

        num5.setOnClickListener {
            resultado.text = resultado.text.toString() + "5"
        }

        num6.setOnClickListener {
            resultado.text = resultado.text.toString() + "6"
        }

        num7.setOnClickListener {
            resultado.text = resultado.text.toString() + "7"
        }

        num8.setOnClickListener {
            resultado.text = resultado.text.toString() + "8"
        }

        num9.setOnClickListener {
            resultado.text = resultado.text.toString() + "9"
        }

        botonSum.setOnClickListener {
            resultado.text = resultado.text.toString() + "+"
        }

        botonResta.setOnClickListener {
            resultado.text = resultado.text.toString() + "-"
        }

        botonMult.setOnClickListener {
            resultado.text = resultado.text.toString() + "x"
        }

        botonDiv.setOnClickListener {
            resultado.text = resultado.text.toString() + "/"
        }

        botonIgual.setOnClickListener {

            var solucion = 0.0

            if (resultado.text.contains("+")) {
                val factores = resultado.text.split("+")
                solucion = factores[0].toDouble() + factores[1].toDouble()
            } else if (resultado.text.contains("-")) {
                val factores = resultado.text.split("-")
                solucion = factores[0].toDouble() - factores[1].toDouble()
            } else if (resultado.text.contains("x")) {
                val factores = resultado.text.split("x")
                solucion = factores[0].toDouble() * factores[1].toDouble()
            } else if (resultado.text.contains("/")) {
                val factores = resultado.text.split("/")
                solucion = factores[0].toDouble() / factores[1].toDouble()
            }

            if (solucion.toString().substring(solucion.toString().length - 1, solucion.toString().length) = ".0")

            resultado.text = solucion.toString()

        }
    }
}