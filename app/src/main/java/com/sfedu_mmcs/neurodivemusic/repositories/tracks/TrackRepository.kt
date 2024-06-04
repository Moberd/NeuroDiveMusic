package com.sfedu_mmcs.neurodivemusic.repositories.tracks

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sfedu_mmcs.neurodivemusic.constants.getTracksUrl
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URL
import javax.inject.Inject


object StorageKeys {
    const val sharedPreferencesKey = "TrackPreferences"
    const val tracks = "tracks"
    const val history = "history"
    const val favorites = "favorites"
}

class TrackRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(StorageKeys.sharedPreferencesKey, Context.MODE_PRIVATE)
    private val gson = Gson()

    private var tracksList = getEntities()

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
                launch(Dispatchers.IO) {
                    response = URL(getTracksUrl()).readText()
                } // launch
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
        val json = sharedPreferences.getString(StorageKeys.favorites, null)

        Log.i("123.Json", json.toString())
        val favorites = if (json != null) {
            gson.fromJson<MutableList<String>>(
                json,
                object : TypeToken<MutableList<String>>() {}.type
            )
        } else mutableListOf()

        favorites.add(id)

        sharedPreferences.edit().putString(StorageKeys.favorites, gson.toJson(favorites)).apply()
    }


    fun deleteFromFavorites(id: String) {
        val json = sharedPreferences.getString(StorageKeys.favorites, null)

        val favorites = if (json != null) {
            gson.fromJson<MutableList<String>>(
                json,
                object : TypeToken<MutableList<String>>() {}.type
            )
        } else mutableListOf()

        favorites.remove(id)

        sharedPreferences.edit().putString(StorageKeys.favorites, gson.toJson(favorites)).apply()
    }

    fun getTrack(id: String): TrackData? {
        val track = tracksList.find { it -> it.id == id }
        lastId = id

        return track
    }

    fun fetchFavorites(): List<TrackData> {
        return runBlocking {
            val json = sharedPreferences.getString(StorageKeys.favorites, null)

            if (json == null) return@runBlocking emptyList()

            val favorites =
                gson.fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type)

            Log.i("123.favorites", json.toString())

            val tracks = getEntities()

            return@runBlocking tracks.filter { track ->
                val founded = favorites.find { id -> track.id == id }

                return@filter founded != null
            }
        }

    }
}

