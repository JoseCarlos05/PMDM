package com.example.ejercicio1

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.ContentValues
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest


class MainActivity : AppCompatActivity() {

    private lateinit var imagen: ImageView
    private lateinit var ruta: TextView
    private var imageUri: Uri? = null

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 1002
    }

    @SuppressLint("SetTextI18n")
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            imageUri?.let {
                imagen.setImageURI(it)
                ruta.text = "Ruta de la imagen: $it"
            }
        } else {
            Log.e("MainActivity", "La captura de la foto falló")
        }
    }

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

        imagen = findViewById(R.id.imagen)
        ruta = findViewById(R.id.ruta)
        val botonFoto: Button = findViewById(R.id.botonFoto)
        val botonSelect: Button = findViewById(R.id.botonSelect)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_CODE
            )
        }

        botonFoto.setOnClickListener { openCamera() }

        botonSelect.setOnClickListener { openGallery() }

    }

    private fun openCamera() {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Nueva Foto")
            put(MediaStore.Images.Media.DESCRIPTION, "Foto capturada desde la cámara")
        }

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let {
            takePicture.launch(it)
        }
    }

    @SuppressLint("IntentReset")
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        selectImage.launch(galleryIntent)
    }

    @SuppressLint("SetTextI18n")
    private val selectImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let {
                imagen.setImageURI(it)
                ruta.text = "Ruta de la imagen: $it"
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Permisos concedidos.")
            } else {
                Log.d("MainActivity", "Permisos denegados.")
            }
        }
    }
}