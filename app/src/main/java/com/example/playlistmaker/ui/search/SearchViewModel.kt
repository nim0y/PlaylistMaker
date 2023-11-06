package com.example.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.search.HistoryInteractor
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.utils.DEBOUNCE_DELAY

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val _searchState = MutableLiveData<State>()
    val searchState: LiveData<State> = _searchState
    private val _historyState = MutableLiveData(historyInteractor.read())
    val historyState: LiveData<List<Track>> = _historyState

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private var lastQuery: String? = null

    init {
        setState(State.SomeHistory(historyInteractor.read()))
    }

    private fun setState(state: State) {
        _searchState.postValue(state)
    }

    fun queryDebounce(queryNew: String?) {
        lastQuery = queryNew
        if (queryNew.isNullOrEmpty()) {
            setState(State.SomeHistory(historyInteractor.read()))
        } else {
            setState(State.Load)
            searchDebounce { searchRequest(queryNew) }
        }
    }

    private fun searchDebounce(request: () -> Unit) {
        searchRunnable = Runnable { request() }
        handler.removeCallbacksAndMessages(TOKEN)
        val time = SystemClock.uptimeMillis() + DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable!!, TOKEN, time)
    }

    fun onDestroyHandlerRemove() {
        handler.removeCallbacksAndMessages(Any())
    }

    fun addTrackToHistory(track: Track) {
        historyInteractor.add(track)
        setState(State.SomeHistory(historyInteractor.read()))
    }

    private fun historyModification() {
        setState(State.SomeHistory(historyInteractor.read()))
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(searchRunnable)
    }

    fun toClearSearchHistory() {
        historyInteractor.clear()
        historyModification()
    }

    private fun searchRequest(queryNew: String) {
        if (lastQuery!!.isBlank()) {
            setState(State.SomeHistory(historyInteractor.read()))
        } else {
            searchInteractor.searchTracks(
                queryNew, object : SearchInteractor.TrackConsumer {
                    override fun consume(found: List<Track>?, errorId: String?) {
                        handler.post {
                            when {
                                errorId != null -> {
                                    setState(State.Error)
                                }

                                found.isNullOrEmpty() -> {
                                    setState(State.NothingFound())
                                }

                                else -> {
                                    setState(State.SomeData(found))
                                }
                            }

                        }
                    }
                }
            )
        }
    }

    companion object {
        val TOKEN = Any()
    }
}