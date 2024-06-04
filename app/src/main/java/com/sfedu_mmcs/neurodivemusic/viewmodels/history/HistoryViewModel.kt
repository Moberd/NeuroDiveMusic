package com.sfedu_mmcs.neurodivemusic.viewmodels.history

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.repositories.tracks.TrackRepository
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.model.HistoryTrackData
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val trackRepository: TrackRepository
) : ViewModel() {
    val historyList = MutableLiveData<List<HistoryTrackData>>(emptyList())


}
