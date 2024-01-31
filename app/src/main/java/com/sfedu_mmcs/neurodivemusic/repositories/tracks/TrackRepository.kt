package com.sfedu_mmcs.neurodivemusic.repositories.tracks

import android.content.Context
import android.content.SharedPreferences
import androidx.core.graphics.drawable.toDrawable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData

val trackMocks = listOf<TrackData>(
    TrackData(
        id="tUBVEKzsZ-k",
        artist="Альянс",
        name="На Заре",
        duration=10,
        cover= R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="HvZrAOIW5W8",
        artist="Смысловые Галлюцинации",
        name="Вечно молодой",
        duration=10,
        cover= R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="L7LTe7LbKT0",
        artist="Chernikovskaya Hata",
        name="Белая ночь",
        duration=10,
        cover= R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="1M_k7b1cAxM",
        artist="Chernikovskaya Hata",
        name="Ты не верь слезам",
        duration=10,
        cover= R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="vtYiwVpf9Mo",
        artist="Chernikovskaya Hata",
        name="Мальчик мой",
        duration=10,
        cover= R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="1qGbAm5kYyM",
        artist="Chernikovskaya Hata",
        name="Ночное рандеву",
        duration=10,
        cover= R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="Ic5YEw_QzGs",
        artist="Chernikovskaya Hata",
        name="Нажми на кнопку",
        duration=10,
        cover= R.drawable.logo.toDrawable()
    ),
)

class TrackRepository(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("TrackPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveEntities(tracks: List<TrackData>) {
        val json = gson.toJson(tracks)
        sharedPreferences.edit().putString("tracks", json).apply()
    }

    fun getEntities(): List<TrackData> {
        val json = sharedPreferences.getString("tracks", null)
        return if (json != null) {
            gson.fromJson(json, object : TypeToken<List<TrackData>>() {}.type)
        } else {
            emptyList()
        }
    }

    init {
        saveEntities(trackMocks)
    }
}