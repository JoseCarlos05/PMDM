package com.example.ejercicio4

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.database.Cursor
import android.Manifest
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gridView = findViewById(R.id.gridView)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            val imagePaths = cargarImagenes(this)
            val adapter = object : ArrayAdapter<String>(this, R.layout.item_imagen, imagePaths) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false)
                    val imageView: ImageView = view.findViewById(R.id.imageView)
                    val imagePath = getItem(position)

                    imagePath?.let {
                        val bitmap = BitmapFactory.decodeFile(it)
                        imageView.setImageBitmap(bitmap)
                    }

                    return view
                }
            }
            gridView.adapter = adapter
        }
    }

    private fun cargarImagenes(context: Context): List<String> {
        val imageList = mutableListOf<String>()

        val contentResolver = context.contentResolver
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val cursor = contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA),
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val imagePath = it.getString(columnIndex)
                imageList.add(imagePath)
            }
        }

        return imageList
    }
}