package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var searchQuery: String = QUERY_DEF
    private var lastSearchQuery: String? = null
    private var currentCall: Call<TracksResponse>? = null

    private lateinit var inputEditText: EditText
    private lateinit var tracksRecyclerView: RecyclerView
    private lateinit var placeholder: View
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var refreshButton: View

    private val adapter = TrackAdapter()

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val QUERY_DEF = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        tracksRecyclerView = findViewById(R.id.tracksRecyclerView)
        placeholder = findViewById(R.id.placeholder)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderText = findViewById(R.id.placeholderText)
        refreshButton = findViewById(R.id.refreshButton)

        tracksRecyclerView.layoutManager = LinearLayoutManager(this)
        tracksRecyclerView.adapter = adapter

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            clearScreen()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = inputEditText.text.toString()
                if (query.isNotEmpty()) {
                    performSearch(query)
                }
                true
            } else {
                false
            }
        }

        refreshButton.setOnClickListener {
            lastSearchQuery?.let { performSearch(it) }
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString() ?: ""
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onDestroy() {
        currentCall?.cancel()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_QUERY, QUERY_DEF)
        inputEditText.setText(searchQuery)
    }

    private fun performSearch(query: String) {
        currentCall?.cancel()
        lastSearchQuery = query
        val call = ItunesApiClient.service.search(query)
        currentCall = call
        call.enqueue(object : Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>,
            ) {
                if (response.isSuccessful) {
                    val results = response.body()?.results.orEmpty()
                    if (results.isEmpty()) {
                        showNothingFound()
                    } else {
                        showResults(results)
                    }
                } else {
                    showConnectionError()
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                if (call.isCanceled) return
                showConnectionError()
            }
        })
    }

    private fun showResults(tracks: List<Track>) {
        adapter.submitList(tracks)
        tracksRecyclerView.isVisible = true
        placeholder.isGone = true
    }

    private fun showNothingFound() {
        adapter.submitList(emptyList())
        tracksRecyclerView.isGone = true
        placeholderImage.setImageResource(R.drawable.ic_nothing_found_120)
        placeholderText.setText(R.string.nothing_found)
        refreshButton.isGone = true
        placeholder.isVisible = true
    }

    private fun showConnectionError() {
        adapter.submitList(emptyList())
        tracksRecyclerView.isGone = true
        placeholderImage.setImageResource(R.drawable.ic_no_internet_120)
        placeholderText.setText(R.string.connection_error)
        refreshButton.isVisible = true
        placeholder.isVisible = true
    }

    private fun clearScreen() {
        adapter.submitList(emptyList())
        tracksRecyclerView.isGone = true
        placeholder.isGone = true
    }
}
