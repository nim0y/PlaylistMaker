package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_QUERY_HISTORY = "SEARCH_QUERY_HISTORY"
        const val PREF_NAME = "pref_name"
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
    private var searchHistoryLayout: LinearLayout? = null
    private var searchHistoryRecyclerView: RecyclerView? = null
    private var sharedPreferencesHistory: SharedPreferences? = null
    private lateinit var searchHistoryClass: SearchHistory

    private val tracksList = ArrayList<Track>()
    private var searchHistoryList = ArrayList<Track>()
    private lateinit var adapter: TrackAdapter
    private lateinit var searchAdapter: TrackAdapter

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_HISTORY, currentSearchQuery)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQueryText?.setText(savedInstanceState.getString(SEARCH_QUERY_HISTORY))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchHistoryLayout = findViewById(R.id.search_history_layout)
        searchHistoryRecyclerView = findViewById(R.id.search_history_recycle_view)
        searchQueryText = findViewById(R.id.edit_query)
        trackRecyclerView = findViewById(R.id.track_recycler_view)

        val clearHistoryButton = findViewById<Button>(R.id.history_clear_button)
        val errorNoConnection = findViewById<LinearLayout>(R.id.no_connection_error_layout)
        val nothingFoundCase = findViewById<LinearLayout>(R.id.nothing_found_case_layout)
        val searchRefreshButton = findViewById<Button>(R.id.search_refresh_button)
        val backButton = findViewById<Button>(R.id.buttonBackSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)


        sharedPreferencesHistory = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        searchHistoryClass = SearchHistory(sharedPreferencesHistory!!)

        adapter = TrackAdapter(tracksList) { trackList ->
            searchHistoryClass.add(trackList)
        }

        trackRecyclerView.adapter = adapter

        searchAdapter = TrackAdapter(searchHistoryList) { searchHistoryList ->
            searchHistoryClass.add(searchHistoryList)
            readHistory()
            searchAdapter.notifyItemRangeChanged(0, 10)
        }
        searchHistoryRecyclerView?.adapter = searchAdapter

        fun sendToServer() {
            trackRecyclerView.visibility = View.VISIBLE
            nothingFoundCase.visibility = View.GONE
            errorNoConnection.visibility = View.GONE
            searchHistoryLayout?.visibility = View.GONE
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
            Log.e("myLog", "Clear button + $searchHistoryList")
            searchAdapter.notifyDataSetChanged()
            searchQueryText?.clearFocus()
            trackRecyclerView.visibility = View.GONE
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                currentSearchQuery = searchQueryText?.text.toString()
                searchHistoryLayout?.visibility = searchHistoryLayoutVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }

        searchQueryText?.addTextChangedListener(textWatcher)

        searchQueryText?.setOnFocusChangeListener { _, hasFocus ->
            readHistory()
            trackRecyclerView.visibility = View.GONE
            if (searchHistoryList.isNotEmpty() && hasFocus && searchQueryText?.text.isNullOrEmpty()) {
                searchHistoryLayout?.visibility = View.VISIBLE
            } else View.GONE
        }

        searchQueryText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchQueryText?.text?.isNotEmpty() == true) {
                    sendToServer()
                } else {
                    readHistory()
                }
            }
            false
        }
        searchRefreshButton.setOnClickListener {
            sendToServer()
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryClass.clear()
            searchHistoryList.clear()
            searchAdapter.notifyItemRangeChanged(0, searchHistoryList.size)
            searchHistoryLayout?.visibility = View.GONE
        }
    }

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readHistory() {
        searchHistoryList.clear()
        searchHistoryList.addAll(searchHistoryClass.read())
        searchAdapter.notifyItemRangeChanged(0, searchHistoryList.size)
        Log.e("myLog", "readHistory + $searchHistoryList")
    }

    fun searchHistoryLayoutVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty() && searchHistoryList.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}