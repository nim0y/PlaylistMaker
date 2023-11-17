package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.ui.player.AudioPlayerActivity
import com.example.playlistmaker.ui.search.SearchViewModel
import com.example.playlistmaker.ui.search.State
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.utils.CLICK_DEBOUNCE
import com.example.playlistmaker.utils.SEARCH_QUERY_HISTORY
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private var binding: ActivitySearchBinding? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val searchResAdapter = TrackAdapter()
    private val searchHistoryAdapter = TrackAdapter()
    private var currentSearchQuery: String? = null
    private var textWatcher: TextWatcher? = null
    private val vm by viewModel<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.trackRecyclerView?.adapter = searchResAdapter
        binding?.trackRecyclerView?.itemAnimator = null
        binding?.searchHistoryRecycleView?.adapter = searchHistoryAdapter
        binding?.searchHistoryRecycleView?.itemAnimator = null

        searchResAdapter.itemClickListener = {
            if (clickDebounce()) {
                vm.addTracktoHistoryInvisible(it)
                openAudioPlayer(track = it)
            }
        }
        searchHistoryAdapter.itemClickListener = {
            if (clickDebounce()) {
                vm.addTrackToHistory(it)
                openAudioPlayer(it)
            }
        }

        binding?.toolbarSearch?.setNavigationOnClickListener {
            finish()
        }
        binding?.clearIcon?.setOnClickListener {
            binding?.editQuery?.text?.clear()
            closeKeyboard()
            binding?.trackRecyclerView?.isVisible = false
            binding?.progressBar?.isVisible = false
            binding?.nothingFoundCaseLayout?.isVisible = false
            binding?.noConnectionErrorLayout?.isVisible = false
            binding?.searchHistoryLayout?.isVisible = false
            searchHistoryAdapter.notifyItemRangeChanged(0, 10)
            binding?.editQuery?.clearFocus()

        }
        binding?.searchRefreshButton?.setOnClickListener {
            vm.queryDebounce(currentSearchQuery)
        }

        binding?.historyClearButton?.setOnClickListener {
            vm.toClearSearchHistory()
            searchHistoryAdapter.notifyItemRangeChanged(0, 10)
        }
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding?.clearIcon?.visibility = clearButtonVisibility(p0)
                binding?.searchHistoryLayout?.visibility =
                    if (binding?.editQuery?.hasFocus() == true && p0?.isEmpty() == true) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                val queryNew = p0?.toString()
                if (currentSearchQuery != queryNew) {
                    currentSearchQuery = queryNew
                    vm.queryDebounce(queryNew ?: "")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        textWatcher.let {
            binding?.editQuery?.addTextChangedListener(it)
        }
        binding?.editQuery?.setOnFocusChangeListener { _, _ ->
            vm.historyModification()
        }

        vm.historyState.observe(this) {
            showHistory(it)

        }
        vm.searchState.observe(this) {
            executeState(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroyHandlerRemove()
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding?.editQuery?.windowToken, 0)
    }

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun openAudioPlayer(track: Track) {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(SEARCH_QUERY_HISTORY, track)
            startActivity(intent)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE)
        }
        return current
    }

    private fun executeState(state: State) {
        when (state) {
            is State.SomeData -> showSomeData(state.tracks)
            is State.SomeHistory -> showHistory(state.tracks)
            is State.Error -> showNoConnection()
            is State.Load -> showProgressBar()
            is State.NothingFound -> showNothing()
        }
    }

    private fun showProgressBar() {
        binding?.noConnectionErrorLayout?.isVisible = false
        binding?.nothingFoundCaseLayout?.isVisible = false
        binding?.searchHistoryLayout?.isVisible = false
        binding?.trackRecyclerView?.isVisible = false
        binding?.progressBar?.isVisible = true
        with(searchResAdapter) {
            tracksList.clear()
            notifyItemRangeChanged(0, 50)
        }
    }

    private fun showNothing() {
        binding?.noConnectionErrorLayout?.isVisible = false
        binding?.nothingFoundCaseLayout?.isVisible = true
        binding?.searchHistoryLayout?.isVisible = false
        binding?.trackRecyclerView?.isVisible = false
        binding?.progressBar?.isVisible = false

    }

    private fun showSomeData(found: List<Track>?) {
        binding?.noConnectionErrorLayout?.isVisible = false
        binding?.nothingFoundCaseLayout?.isVisible = false
        binding?.searchHistoryLayout?.isVisible = false
        binding?.trackRecyclerView?.isVisible = true
        binding?.progressBar?.isVisible = false
        with(searchResAdapter) {
            tracksList.clear()
            tracksList.addAll(found!!)
            notifyItemRangeChanged(0, found.size)
        }
    }

    private fun showHistory(historyList: List<Track>) {
        if (historyList.isEmpty()) {
            binding?.searchHistoryLayout?.isVisible = false
        } else if (binding?.editQuery?.hasFocus() == true) {
            binding?.noConnectionErrorLayout?.isVisible = false
            binding?.nothingFoundCaseLayout?.isVisible = false
            binding?.searchHistoryLayout?.isVisible = true
            binding?.trackRecyclerView?.isVisible = false
            binding?.progressBar?.isVisible = false

            with(searchHistoryAdapter) {
                tracksList.clear()
                tracksList.addAll(historyList)
                notifyItemRangeChanged(0, historyList.size)
            }
        }
    }

    private fun showNoConnection() {
        binding?.noConnectionErrorLayout?.isVisible = true
        binding?.nothingFoundCaseLayout?.isVisible = false
        binding?.searchHistoryLayout?.isVisible = false
        binding?.trackRecyclerView?.isVisible = false
        binding?.progressBar?.isVisible = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_HISTORY, currentSearchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding?.editQuery?.setText(savedInstanceState.getString(SEARCH_QUERY_HISTORY))
    }
}