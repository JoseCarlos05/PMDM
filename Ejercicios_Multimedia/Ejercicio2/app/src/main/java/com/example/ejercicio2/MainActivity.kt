package com.example.ejercicio2

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioFilePath: String
    private lateinit var statusMessage: TextView
    private lateinit var btnRecord: Button
    private lateinit var btnStop: Button
    private lateinit var btnPlay: Button
    private lateinit var btnStopPlay: Button
    private var permissionToRecordAccepted = false
    private var fileName: String = ""
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajuste de padding para los sistemas de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Establecer la ruta del archivo de audio
        if (externalCacheDir == null) {
            Log.e(LOG_TAG, "Invalid file path")
            return
        }
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        audioFilePath = fileName

        // Solicitar permisos al inicio
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        // Configurar botones
        statusMessage = findViewById(R.id.statusMessage)
        btnRecord = findViewById(R.id.btnRecord)
        btnStop = findViewById(R.id.btnStop)
        btnPlay = findViewById(R.id.btnPlay)
        btnStopPlay = findViewById(R.id.btnStopPlay)

        btnRecord.setOnClickListener { startRecording() }
        btnStop.setOnClickListener { stopRecording() }
        btnPlay.setOnClickListener { startPlaying() }
        btnStopPlay.setOnClickListener { stopPlaying() }
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

    private fun startPlaying() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioFilePath)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed: ${e.message}")
            }
        }
    }

    private fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun startRecording() {
        // Verificar si tenemos el permiso
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            mediaRecorder = MediaRecorder().apply {
                try {
                    setAudioSource(MediaRecorder.AudioSource.MIC)  // Fuente de audio: MIC
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)  // Formato de salida
                    setOutputFile(fileName)  // Archivo de salida
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)  // Codificador de audio

                    prepare()
                    start()
                    Log.d(LOG_TAG, "Recording started")

                    statusMessage.text = "Grabando..."
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
}
