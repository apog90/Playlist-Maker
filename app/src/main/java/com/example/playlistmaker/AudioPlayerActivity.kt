package com.example.playlistmaker

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AudioPlayerActivity : AppCompatActivity() {

    private val durationFormatter = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audioPlayer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        val track = intent.getSerializableExtra(EXTRA_TRACK) as? Track
        if (track == null) {
            finish()
            return
        }

        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }

        findViewById<TextView>(R.id.trackName).text = track.trackName.orEmpty()
        findViewById<TextView>(R.id.artistName).text = track.artistName.orEmpty()

        val durationLabel = findViewById<TextView>(R.id.durationLabel)
        val durationValue = findViewById<TextView>(R.id.durationValue)
        val trackTimeMillis = track.trackTimeMillis
        if (trackTimeMillis == null) {
            durationLabel.isGone = true
            durationValue.isGone = true
        } else {
            durationValue.text = durationFormatter.format(trackTimeMillis)
        }

        val albumLabel = findViewById<TextView>(R.id.albumLabel)
        val albumValue = findViewById<TextView>(R.id.albumValue)
        val collectionName = track.collectionName
        if (collectionName.isNullOrBlank()) {
            albumLabel.isGone = true
            albumValue.isGone = true
        } else {
            albumValue.text = collectionName
        }

        val yearLabel = findViewById<TextView>(R.id.yearLabel)
        val yearValue = findViewById<TextView>(R.id.yearValue)
        val releaseDate = track.releaseDate
        if (releaseDate.isNullOrBlank()) {
            yearLabel.isGone = true
            yearValue.isGone = true
        } else {
            yearValue.text = releaseDate.take(4)
        }

        findViewById<TextView>(R.id.genreValue).text = track.primaryGenreName.orEmpty()
        findViewById<TextView>(R.id.countryValue).text = track.country.orEmpty()

        val cover = findViewById<ShapeableImageView>(R.id.cover)
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder_player_312)
            .error(R.drawable.ic_placeholder_player_312)
            .into(cover)
    }

    companion object {
        const val EXTRA_TRACK = "track"
    }
}
