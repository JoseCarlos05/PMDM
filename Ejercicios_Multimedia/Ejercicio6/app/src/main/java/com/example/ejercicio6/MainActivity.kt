package com.example.ejercicio6

import android.os.Bundle
import android.widget.GridView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var ruta: TextView
    private val PERMISSION_REQUEST_CODE = 1

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

        gridView = findViewById(R.id.gridView)
        ruta = findViewById(R.id.ruta)

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
            val mediaPaths = cargarMedios(this)
            val adapter = object : ArrayAdapter<String>(this, R.layout.item_imagen, mediaPaths) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false)
                    val imageView: ImageView = view.findViewById(R.id.imageView)
                    val mediaPath = getItem(position)

                    mediaPath?.let {
                        val fileUri = Uri.parse(it)
                        if (it.endsWith(".jpg") || it.endsWith(".png")) {
                            val bitmap = BitmapFactory.decodeFile(it)
                            imageView.setImageBitmap(bitmap)
                        } else {
                            imageView.setImageResource(R.drawable.ic_launcher_background)
                        }
                    }

                    return view
                }
            }
            gridView.adapter = adapter

            gridView.setOnItemClickListener { _, _, position, _ ->
                val selectedItem = mediaPaths[position]
                ruta.text = "Ruta: $selectedItem"
            }
        }
    }

    private fun cargarMedios(context: Context): List<String> {
        val mediaList = mutableListOf<String>()

        val contentResolver = context.contentResolver

        val uriImages: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val uriVideos: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val uriAudio: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        mediaList.addAll(queryMedia(contentResolver, uriImages))

        mediaList.addAll(queryMedia(contentResolver, uriVideos))

        mediaList.addAll(queryMedia(contentResolver, uriAudio))

        return mediaList
    }

    private fun queryMedia(contentResolver: android.content.ContentResolver, uri: Uri): List<String> {
        val mediaList = mutableListOf<String>()
        val cursor: Cursor? = contentResolver.query(
            uri,
            arrayOf(MediaStore.MediaColumns.DATA),
            null,
            null,
            MediaStore.MediaColumns.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val columnIndex = it.getColumnIndex(MediaStore.MediaColumns.DATA)

            while (it.moveToNext()) {
                val filePath = it.getString(columnIndex)
                mediaList.add(filePath)
            }
        }

        return mediaList
    }

}