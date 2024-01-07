package com.sfedu_mmcs.neurodivemusic.viewmodels.music

import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData

val trackMocks = listOf<TrackData>(
    TrackData("1", "first artist", "some long track name", 10, R.drawable.logo.toDrawable()),
    TrackData(
        "2",
        "second artist",
        "some long track name",
        10,
        R.drawable.button_gradient.toDrawable()
    ),
    TrackData("3", "third artist", "some long track name", 10, R.drawable.logo.toDrawable())
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

    init {
        currentTrack.value = trackMocks[index]
    }
}