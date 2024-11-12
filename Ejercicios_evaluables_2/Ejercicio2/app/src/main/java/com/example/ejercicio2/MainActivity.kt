package com.example.ejercicio2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
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

        val gridView = findViewById<GridView>(R.id.gridView)
        val imagenes = arrayOf(R.drawable.images, R.drawable.hojas, R.drawable.paint, R.drawable.images, R.drawable.hojas, R.drawable.paint, R.drawable.images, R.drawable.hojas, R.drawable.paint)

        val adaptador = object : BaseAdapter() {
            override fun getCount(): Int {
                return imagenes.size
            }

            override fun getItem(position: Int): Any {
                return imagenes[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView ?: LayoutInflater.from(baseContext).inflate(R.layout.image_list, parent, false)

                val imageButton = view.findViewById<ImageButton>(R.id.imagen)
                imageButton.setImageResource(imagenes[position])

                imageButton.setOnClickListener {

                    val dialogBuilder = AlertDialog.Builder(parent?.context ?: return@setOnClickListener)
                    dialogBuilder.setTitle("Imagen completa")

                    val dialogView = LayoutInflater.from(parent.context).inflate(R.layout.image_full, null)

                    val fullImageView = dialogView.findViewById<ImageView>(R.id.imagenCompleta)
                    fullImageView.setImageResource(imagenes[position])

                    dialogBuilder.setView(dialogView)
                    dialogBuilder.setNegativeButton("Cerrar") { dialog, _ -> dialog.dismiss() }

                    dialogBuilder.create().show()
                }

                return view
            }
        }

        gridView.adapter = adaptador
    }
}