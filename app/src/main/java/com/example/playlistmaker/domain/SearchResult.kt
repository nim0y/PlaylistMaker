package com.example.playlistmaker.domain

sealed class SearchResult<T>(val result: T? = null, val errorId: String? = null) {
    class Success<T>(result: T) : SearchResult<T>(result)
    class Fail<T>(errorId: String, result: T? = null) : SearchResult<T>(result, errorId)
}