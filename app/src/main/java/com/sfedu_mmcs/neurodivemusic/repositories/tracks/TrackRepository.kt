package com.sfedu_mmcs.neurodivemusic.repositories.tracks

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.model.HistoryTrackData
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val mockCover =
    "https://memoteka.com/images/thumb/9/99/%D0%9F%D0%B5%D0%BF%D0%B50.jpg/300px-%D0%9F%D0%B5%D0%BF%D0%B50.jpg"

val trackMocks = listOf<TrackData>(
    TrackData(
        id = "tUBVEKzsZ-k",
        artist = "Альянс",
        name = "На Заре",
    ),
    TrackData(
        id = "HvZrAOIW5W8",
        artist = "Смысловые Галлюцинации",
        name = "Вечно молодой",
    ),
    TrackData(
        id = "1M_k7b1cAxM",
        artist = "Chernikovskaya Hata",
        name = "Ты не верь слезам",
    ),
    TrackData(
        id = "rrqjcTX78NY",
        artist = "ROOS + BERG",
        name = "No One Left to Love",
    ),
    TrackData(
        id = "iVdVZfpPXds",
        artist = "Смешарики",
        name = "От винта! (Dance Remix)",
    ),
    TrackData(
        id = "2LOmFBBq4T0",
        artist = "The Living Tombstone",
        name = "Dog of Wisdom Remix BLUE feat. Joe Gran",
    ),
    TrackData(
        id = "7xkM8mWC4Kk",
        artist = "Hollow Knight OST",
        name = "Hornet",
    ),
    TrackData(
        id = "Mq8E_1LkoAc",
        artist = "Hollow Knight OST",
        name = "Nightmare King",
    ),
    TrackData(
        id = "cwJKjuyLv80",
        artist = "Enjoykin",
        name = "Ламповая Няша",
    ),
    TrackData(
        id = "jhExvE5fvJw",
        artist = "Eminem & Noisestorm",
        name = "Crab God",
    ),
    TrackData(
        id = "oT3mCybbhf0",
        artist = "AVICII & RICK ASTLEY",
        name = "Never Gonna Wake You Up",
    ),
)

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
    }

    private var lastId: String? = null

    private fun getEntities(): List<TrackData> {
        val json = sharedPreferences.getString(StorageKeys.tracks, null)

        val tracksFromStorage = json?.let {
            gson.fromJson<List<TrackData>>(it, object : TypeToken<List<TrackData>>() {}.type)
        } ?: trackMocks

        saveEntities(tracksFromStorage)

        return tracksFromStorage

    }


    fun getNextTrack(): TrackData {
        val restList = tracksList.filter { it.id != lastId }
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
        if (history.isEmpty()) {
            val json = sharedPreferences.getString(StorageKeys.history, null)

            history = if (json != null) {
                gson.fromJson(json, object : TypeToken<List<HistoryTrackData>>() {}.type)
            } else {
                emptyList()
            }

        }

        return history
    }

    fun sendHistory(track: HistoryTrackData) {
        val newHistory = history.toMutableList()
        newHistory.add(track)
        history = newHistory.toList()

        val json = gson.toJson(history)
        sharedPreferences.edit().putString(StorageKeys.history, json).apply()
    }
}

