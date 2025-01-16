package com.example.ejercicio8

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var takePhotoButton: Button

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioFilePath: String
    private lateinit var mensaje: TextView
    private lateinit var btnRecord: Button
    private lateinit var btnStop: Button
    private lateinit var btnPlay: Button
    private lateinit var btnStopPlay: Button
    private var permissionToRecordAccepted = false
    private var fileName: String = ""
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationTextView: TextView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                locationTextView.text = "Permiso de ubicación denegado."
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

        locationTextView = findViewById(R.id.locationTextView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (externalCacheDir == null) {
            Log.e(LOG_TAG, "Invalid file path")
            return
        }
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        audioFilePath = fileName

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        imageView = findViewById(R.id.imageView)
        mensaje = findViewById(R.id.mensaje)
        btnRecord = findViewById(R.id.btnRecord)
        btnStop = findViewById(R.id.btnStop)
        btnPlay = findViewById(R.id.btnPlay)
        btnStopPlay = findViewById(R.id.btnStopPlay)
        takePhotoButton = findViewById(R.id.takePhotoButton)

        btnRecord.setOnClickListener { startRecording() }
        btnStop.setOnClickListener { stopRecording() }
        btnPlay.setOnClickListener { startPlaying() }
        btnStopPlay.setOnClickListener { stopPlaying() }

        takePhotoButton.setOnClickListener {
            takePhoto()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) {
            Log.e(LOG_TAG, "Permission to record not granted")
            finish()
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePictureIntent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val photo = result.data?.extras?.get("data") as? Bitmap
            imageView.setImageBitmap(photo)
        }
    }

    private fun startPlaying() {
        mediaPlayer = MediaPlayer().apply {
            try {
                mensaje.text = "Reproduciendo audio..."
                btnStop.isEnabled = false
                btnRecord.isEnabled = false
                btnPlay.isEnabled = false
                btnStopPlay.isEnabled = true
                setDataSource(audioFilePath)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed: ${e.message}")
            }
        }
    }

    private fun stopPlaying() {
        mensaje.text = "Reproducción pausada"
        btnStop.isEnabled = false
        btnRecord.isEnabled = true
        btnPlay.isEnabled = true
        btnStopPlay.isEnabled = false
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun startRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            mediaRecorder = MediaRecorder().apply {
                try {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setOutputFile(fileName)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                    prepare()
                    start()
                    Log.d(LOG_TAG, "Recording started")

                    mensaje.text = "Grabando..."
                    btnStop.isEnabled = true
                    btnRecord.isEnabled = false
                    btnPlay.isEnabled = false
                } catch (e: IOException) {
                    Log.e(LOG_TAG, "prepare() failed: ${e.message}")
                } catch (e: IllegalStateException) {
                    Log.e(LOG_TAG, "Illegal state: ${e.message}")
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Error starting the recording: ${e.message}")
                }
            }
        } else {
            Log.e(LOG_TAG, "Permission to record not granted")
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            mensaje.text = "Audio finalizado"
            btnStop.isEnabled = false
            btnRecord.isEnabled = true
            btnPlay.isEnabled = true
            release()

        }
        mediaRecorder = null
        Log.d(LOG_TAG, "Recording stopped")
    }

    override fun onStop() {
        super.onStop()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    locationTextView.text = "Latitud: $latitude, Longitud: $longitude"
                } else {
                    locationTextView.text = "No se pudo obtener la ubicación."
                }
            })
    }
}