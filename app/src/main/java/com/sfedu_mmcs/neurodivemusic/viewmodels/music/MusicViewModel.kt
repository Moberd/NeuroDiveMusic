package com.sfedu_mmcs.neurodivemusic.viewmodels.music

import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData

val trackMocks = listOf<TrackData>(
    TrackData(
        id="1",
        videoId="tUBVEKzsZ-k",
        artist="Альянс",
        name="На Заре",
        duration=10,
        cover=R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="2",
        videoId="HvZrAOIW5W8",
        artist="Смысловые Галлюцинации",
        name="Вечно молодой",
        duration=10,
        cover=R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="3",
        videoId="L7LTe7LbKT0",
        artist="Chernikovskaya Hata",
        name="Белая ночь",
        duration=10,
        cover=R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="4",
        videoId="1M_k7b1cAxM",
        artist="Chernikovskaya Hata",
        name="Ты не верь слезам",
        duration=10,
        cover=R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="5",
        videoId="vtYiwVpf9Mo",
        artist="Chernikovskaya Hata",
        name="Мальчик мой",
        duration=10,
        cover=R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="6",
        videoId="1qGbAm5kYyM",
        artist="Chernikovskaya Hata",
        name="Ночное рандеву",
        duration=10,
        cover=R.drawable.logo.toDrawable()
    ),
    TrackData(
        id="7",
        videoId="Ic5YEw_QzGs",
        artist="Chernikovskaya Hata",
        name="Нажми на кнопку",
        duration=10,
        cover=R.drawable.logo.toDrawable()
    ),
)

var index = 0

class MusicViewModel : ViewModel() {
    val currentTrack: MutableLiveData<TrackData?> by lazy {
        MutableLiveData<TrackData?>(null)
    }

    val status: MutableLiveData<PlayStatus> = MutableLiveData(PlayStatus.Pause)

    fun next() {
        index = (index + 1) % trackMocks.size
        currentTrack.value = trackMocks[index]
    }

    fun prev() {
        index = (index + trackMocks.size - 1) % trackMocks.size
        currentTrack.value = trackMocks[index]
    }

    fun togglePlay() {
        status.value = if (status.value == PlayStatus.Play) PlayStatus.Pause else PlayStatus.Play
    }

    fun setPlay() {
        status.value = PlayStatus.Play
    }

    fun setPause() {
        status.value = PlayStatus.Pause
    }

    init {
        currentTrack.value = trackMocks[index]
    }
}