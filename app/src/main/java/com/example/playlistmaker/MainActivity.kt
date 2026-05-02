package com.example.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val searchClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Заглушка клика на Поиск", Toast.LENGTH_SHORT).show()
            }
        }
        searchButton.setOnClickListener(searchClickListener)

        val mediaButton = findViewById<Button>(R.id.media_button)
        mediaButton.setOnClickListener { Toast.makeText(this@MainActivity, "Заглушка клика на Медиатеку", Toast.LENGTH_SHORT).show() }

        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener { Toast.makeText(this@MainActivity, "Заглушка клика на Настройки", Toast.LENGTH_SHORT).show() }
    }
}