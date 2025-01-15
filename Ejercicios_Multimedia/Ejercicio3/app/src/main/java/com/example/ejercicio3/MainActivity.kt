package com.example.ejercicio3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val PERMISSION_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoView: VideoView
    private lateinit var audioFilePath: String
    private var fileName: String = ""
    private lateinit var selectVideoLauncher: ActivityResultLauncher<Intent>
    private var selectedVideoUri: Uri? = null
    private lateinit var selectAudioLauncher: ActivityResultLauncher<Intent>
    private var selectedAudioUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playAudioButton: Button = findViewById(R.id.btn_play_audio)
        val playVideoButton: Button = findViewById(R.id.btn_play_video)
        val btnSelectVideo: Button = findViewById(R.id.btn_select_video)
        val btnSelectAudio: Button = findViewById(R.id.btn_select_audio)
        videoView = findViewById(R.id.video_view)

        if (externalCacheDir == null) {
            Log.e(LOG_TAG, "Invalid file path")
            return
        }
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        audioFilePath = fileName

        playAudioButton.setOnClickListener {
            playAudio()
        }

        playVideoButton.setOnClickListener {
            playVideo()
        }

        btnSelectAudio.setOnClickListener {
            selectAudio()
        }

        btnSelectVideo.setOnClickListener {
            selectVideo()
        }

        selectVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val videoUri: Uri? = result.data?.data
                if (videoUri != null) {
                    try {
                        selectedVideoUri = videoUri
                        videoView.setVideoURI(videoUri)
                        videoView.pause()
                        Toast.makeText(this, "Video cargado. Presiona 'Reproducir Video'.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "Error setting video URI: ${e.message}")
                        Toast.makeText(this, "No se puede reproducir este video", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(LOG_TAG, "No video selected")
                }
            } else {
                Log.e(LOG_TAG, "Video selection canceled")
            }
        }

        selectAudioLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val audioUri: Uri? = result.data?.data
                if (audioUri != null) {
                    try {
                        selectedAudioUri = audioUri
                        Toast.makeText(this, "Audio cargado. Presiona 'Reproducir Audio'.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "Error setting audio URI: ${e.message}")
                        Toast.makeText(this, "No se puede reproducir este audio", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(LOG_TAG, "No audio selected")
                }
            } else {
                Log.e(LOG_TAG, "Audio selection canceled")
            }
        }
    }

    private fun playAudio() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            return
        }
        selectedAudioUri?.let { uri ->
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(this@MainActivity, uri)
                    prepare()
                    start()
                }
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Error al preparar el audio: ${e.message}")
                Toast.makeText(this, "Error al reproducir el audio", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "No se ha seleccionado un audio.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playVideo() {
        selectedVideoUri?.let { uri ->
            try {
                videoView.setVideoURI(uri)
                videoView.start()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al reproducir el video.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "No se ha seleccionado un video.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("IntentReset")
    private fun selectAudio() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "audio/*"
        selectAudioLauncher.launch(galleryIntent)
    }

    @SuppressLint("IntentReset")
    private fun selectVideo() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "video/*"
        selectVideoLauncher.launch(galleryIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}