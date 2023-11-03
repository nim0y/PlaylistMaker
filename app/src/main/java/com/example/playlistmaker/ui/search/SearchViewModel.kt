package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.data.dto.Constants.DEBOUNCE_DELAY
import com.example.playlistmaker.domain.api.search.HistoryInteractor
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.models.search.Track


class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val _searchState = MutableLiveData<State>()
    val searchState: LiveData<State> = _searchState
    private val _historyState = MutableLiveData(historyInteractor.read())
    val historyState: LiveData<List<Track>> = _historyState

    private val handler = Handler(Looper.getMainLooper())

    private fun setState(state: State) {
        _searchState.postValue(state)
    }

    init {
        Log.e("myLog", "------${_searchState.value}")
        setState(State.SomeHistory(historyInteractor.read()))
    }

    fun searchDebounce(queryNew: String) {
        lastQuery = queryNew
        when (lastQuery) {
            queryNew.isNullOrBlank().toString() ->
                setState(State.SomeHistory(historyInteractor.read()))

            else -> {

                val searchRunnable = Runnable { sendToServer(queryNew) }
                handler.removeCallbacks(searchRunnable)
                handler.postAtTime(searchRunnable, TOKEN, DEBOUNCE_DELAY)
            }

        }
    }

    private var lastQuery: String? = null

    fun addTrackToHistory(track: Track) {
        historyInteractor.add(track)
        setState(State.SomeHistory(historyInteractor.read()))
    }

    private fun historyModification() {
        Log.e("myLog", "------${_searchState.value}")
        setState(State.SomeHistory(historyInteractor.read()))
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(TOKEN)
    }

    fun clearSearchHistory() {
        historyInteractor.clear()
        historyModification()
    }

    fun some(queryNew: String) {
        if (queryNew.isNullOrBlank()) {
            Log.e("myLog", "Jopa")
            setState(State.SomeHistory(historyInteractor.read()))
        }
        if (queryNew.isNotEmpty()) {
            setState(State.Load)
            searchDebounce(queryNew)
        }
    }

    @SuppressLint
    fun sendToServer(queryNew: String) {
        Log.e("myLog", "Search request-----$queryNew")
        searchInteractor.searchTracks(
            queryNew, object : SearchInteractor.TrackConsumer {
                override fun consume(found: List<Track>?, errorId: String?) {
                    val tempList = mutableListOf<Track>()
                    handler.post {
                        if (found != null) {
                            tempList.clear()
                            tempList.addAll(found)
                        }
                        when {
                            errorId != null -> {
                                setState(State.Error)
                            }

                            tempList.isEmpty() -> {
                                setState(State.NothingFound())
                            }

                            else -> {
                                Log.e("myLog", "Some Data ${_searchState.value}")
                                setState(State.SomeData(tempList))
                            }
                        }

                    }
                }
            }
        )
    }

    companion object {
        private val TOKEN = Any()
    }
}

