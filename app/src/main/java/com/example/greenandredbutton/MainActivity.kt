package com.example.greenandredbutton

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var redMediaPlayer: MediaPlayer
    private lateinit var greenMediaPlayer: MediaPlayer

    private val redSoundsDir = "red_sounds"
    private val greenSoundsDir = "green_sounds"

    private lateinit var shuffledRedSoundFiles: MutableList<String>
    private lateinit var shuffledGreenSoundFiles: MutableList<String>

    private fun getSoundFilesFromAssets(directory: String): Array<String> {
        return try {
            assets.list(directory) ?: arrayOf()
        } catch (e: Exception) {
            arrayOf()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redMediaPlayer = MediaPlayer()
        greenMediaPlayer = MediaPlayer()

        val redButton: Button = findViewById(R.id.redButton)
        val greenButton: Button = findViewById(R.id.greenButton)

        val redSoundFiles = getSoundFilesFromAssets(redSoundsDir)
        val greenSoundFiles = getSoundFilesFromAssets(greenSoundsDir)

        shuffledRedSoundFiles = redSoundFiles.toMutableList()
        shuffledGreenSoundFiles = greenSoundFiles.toMutableList()

        Collections.shuffle(shuffledRedSoundFiles)
        Collections.shuffle(shuffledGreenSoundFiles)

        redButton.setOnClickListener {
            playNextSound(shuffledRedSoundFiles, redMediaPlayer, redSoundsDir)
        }

        greenButton.setOnClickListener {
            playNextSound(shuffledGreenSoundFiles, greenMediaPlayer, greenSoundsDir)
        }
    }

    private fun playNextSound(soundFiles: MutableList<String>, mediaPlayer: MediaPlayer, directory: String) {
        if (soundFiles.isEmpty()) {
            soundFiles.addAll(getSoundFilesFromAssets(directory).toList())
            Collections.shuffle(soundFiles)
        }

        val soundFile = soundFiles.removeAt(0)

        try {
            val descriptor = assets.openFd("$directory/$soundFile")
            mediaPlayer.reset()
            mediaPlayer.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        redMediaPlayer.release()
        greenMediaPlayer.release()
    }
}
