package com.sfedu_mmcs.neurodivemusic.repositories.tracks

import android.R.attr.data
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sfedu_mmcs.neurodivemusic.constants.getTracksUrl
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.model.HistoryTrackData
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.inject.Inject


object StorageKeys {
    const val sharedPreferencesKey = "TrackPreferences"
    const val tracks = "tracks"
    const val history = "history"
}

class TrackRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(StorageKeys.sharedPreferencesKey, Context.MODE_PRIVATE)
    private val gson = Gson()

    private var tracksList = getEntities()
    private var history: List<HistoryTrackData> = emptyList()

    private fun saveEntities(tracks: List<TrackData>) {
        val json = gson.toJson(tracks)
        sharedPreferences.edit().putString(StorageKeys.tracks, json).apply()
        Log.i("jsonstring", "valid json $json")
    }

    private var lastId: String? = null

    private fun getEntities(): List<TrackData> {
        var response = ""
        runBlocking {
            try {
                withTimeout(5000) {
                    launch(Dispatchers.IO) {
                        response = URL(getTracksUrl()).readText()
                    } // launch
                } // timeout
            } catch (e: Exception) {  // Timeout, permission, URL or network error
                response = "Error"   // Here one uses a not valid message
            }
        }

        val tracksFromStorage = response.let {
            gson.fromJson<List<TrackData>>(it, object : TypeToken<List<TrackData>>() {}.type)
        }

        saveEntities(tracksFromStorage)
        return tracksFromStorage

    }


    fun getNextTrack(genres: List<String>? = null): TrackData {
        Log.d("TrackRepository", "genres filters: [${genres?.joinToString(", ")}]")

        val restList = if (!genres.isNullOrEmpty()) {
            tracksList.filter { it.id != lastId && genres.contains(it.genre) }
        } else {
            tracksList.filter { it.id != lastId }
        }
        Log.d("TrackRepository", "restList: [${restList.joinToString(", ")}]")

        // TODO: consider case when rest list is empty. random() will throw exception
        val newTrack = restList[(restList.indices).random()]
        lastId = newTrack.id

        return newTrack
    }
    fun addToFavorites(id: String) {
        val newTrackList = tracksList.map { if (it.id == id) it.copy(isFavorite = true) else it }
        tracksList = newTrackList
        saveEntities(newTrackList)
    }

    fun fetchHistory(): List<HistoryTrackData> {
        val json = sharedPreferences.getString(StorageKeys.tracks, null)

        history = if (json != null) {
            gson.fromJson(json, object : TypeToken<List<HistoryTrackData>>() {}.type)
        } else {
            emptyList()
        }

        return history.filter { it.isFavorite }
    }

    fun deleteFromFavorites(id: String) {
        val newTrackList = tracksList.map { if (it.id == id) it.copy(isFavorite = false) else it }
        tracksList = newTrackList
        saveEntities(newTrackList)
    }

    fun sendHistory(track: HistoryTrackData) {
        val newHistory = history.toMutableList()
        newHistory.add(track)
        history = newHistory.toList()

        val json = gson.toJson(history)
        //sharedPreferences.edit().putString(StorageKeys.tracks, json).apply()
    }

    fun getTrack(id: String): TrackData? {
        val track = tracksList.find { it -> it.id == id }
        lastId = id

        return track
    }
}

