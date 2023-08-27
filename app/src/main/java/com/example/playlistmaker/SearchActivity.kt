package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY_HISTORY = "SEARCH_QUERY_HISTORY"
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(SearchApi::class.java)

    private var currentSearchQuery = ""
    private var searchQueryText: EditText? = null
    private lateinit var trackRecyclerView: RecyclerView

    private val tracksList = ArrayList<Track>()
    private val adapter = TrackAdapter()


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_HISTORY, currentSearchQuery)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQueryText?.setText(savedInstanceState.getString(SEARCH_QUERY_HISTORY))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchQueryText = findViewById(R.id.edit_query)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        val errorNoConnection = findViewById<LinearLayout>(R.id.no_connection_error_layout)
        val nothingFoundCase = findViewById<LinearLayout>(R.id.nothing_found_case_layout)
        val searchRefreshButton = findViewById<Button>(R.id.search_refresh_button)

        adapter.tracksList = tracksList

        trackRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackRecyclerView.adapter = adapter


        val backButton = findViewById<Button>(R.id.buttonBackSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        fun sendToServer() {
            trackRecyclerView.visibility=View.VISIBLE
            nothingFoundCase.visibility = View.GONE
            errorNoConnection.visibility = View.GONE
            if (searchQueryText?.text?.isNotEmpty()!!) {
                iTunesService.trackSearch(searchQueryText?.text.toString())
                    .enqueue(object : Callback<TrackResponse> {

                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<TrackResponse>,
                            response: Response<TrackResponse>
                        ) {
                            if (response.code() == 200) {
                                tracksList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    tracksList.addAll(response.body()?.results!!)
                                } else {
                                    tracksList.clear()
                                    nothingFoundCase.visibility = View.VISIBLE
                                }
                            } else {
                                tracksList.clear()
                                errorNoConnection.visibility = View.VISIBLE
                            }
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            tracksList.clear()
                            errorNoConnection.visibility = View.VISIBLE
                        }
                    })
            }
        }

        backButton.setOnClickListener {
            finish()
        }


        clearButton.setOnClickListener {
            searchQueryText?.text?.clear()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchQueryText?.windowToken, 0)
            trackRecyclerView.visibility = View.GONE
            searchQueryText?.clearFocus()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                currentSearchQuery = searchQueryText?.text.toString()
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }

        searchQueryText?.addTextChangedListener(textWatcher)

        searchQueryText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendToServer()
            }
            false
        }
        searchRefreshButton.setOnClickListener {
            sendToServer()
        }

    }

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE

        } else {
            View.VISIBLE
        }
    }
}