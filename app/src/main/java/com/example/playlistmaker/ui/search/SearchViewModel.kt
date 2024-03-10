package com.example.playlistmaker.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.search.HistoryInteractor
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.utils.DEBOUNCE_DELAY
import com.example.playlistmaker.utils.Debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val _searchState = MutableLiveData<State>()
    val searchState: LiveData<State> = _searchState
    private val _historyState = MutableLiveData(historyInteractor.read())
    val historyState: LiveData<List<Track>> = _historyState

    private var lastQuery: String? = null
    private val debounceHandler = Debounce()
    private val debounceTrack = debounceHandler.debounce<String>(
        DEBOUNCE_DELAY,
        viewModelScope,
        true,
    ) { searchRequest(it) }


    init {
        setState(State.SomeHistory(historyInteractor.read()))
    }

    private fun setState(state: State) {
        _searchState.postValue(state)
    }

    fun queryDebounce(queryNew: String) {
        lastQuery = queryNew
        if (queryNew.isEmpty()) {
            setState(State.SomeHistory(historyInteractor.read()))
        } else {
            debounceTrack(queryNew)
        }

    }

    fun addTracktoHistoryInvisible(track: Track) {
        historyInteractor.add(track)
    }

    fun addTrackToHistory(track: Track) {
        historyInteractor.add(track)
        setState(State.SomeHistory(historyInteractor.read()))
    }

    fun historyModification() {
        setState(State.SomeHistory(historyInteractor.read()))
    }

    fun toClearSearchHistory() {
        historyInteractor.clear()
        historyModification()
    }

    private fun searchRequest(queryNew: String) {
        setState(State.Load)
        if (lastQuery!!.isBlank()) {
            setState(State.SomeHistory(historyInteractor.read()))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                searchInteractor.searchTracks(queryNew).collect { pair ->
                    when {
                        pair.second != null -> {
                            setState(State.Error)
                        }

                        pair.first.isNullOrEmpty() -> {
                            setState(State.NothingFound)
                        }

                        else -> {
                            setState(State.SomeData(pair.first!!))
                        }
                    }
                }
            }
        }
    }
}