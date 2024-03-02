package com.sfedu_mmcs.neurodivemusic.viewmodels.history

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.repositories.tracks.TrackRepository
import com.sfedu_mmcs.neurodivemusic.repositories.tracks.mockCover
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.model.HistoryTrackData
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val trackRepository: TrackRepository
) : ViewModel() {
    val historyList = MutableLiveData<List<HistoryTrackData>>(emptyList())

    fun sendHistory(track: HistoryTrackData) {
        trackRepository.sendHistory(track)

        val newList = historyList.value?.toMutableList()

        newList?.add(track)
        historyList.value = newList?.toList()
    }

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            historyList.value = trackRepository.fetchHistory()
        }, 1000)
    }
}
