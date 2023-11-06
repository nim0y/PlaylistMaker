package com.example.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest

class RetrofitNetworkClient(private val context: Context, private val iTunesService: SearchApi) :
    NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return if (dto is TracksSearchRequest) {
            val resp = iTunesService.trackSearch(dto.expression).execute()
            val body = resp.body() ?: Response()
            body.apply {
                resultCode = resp.code()
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
    }
}