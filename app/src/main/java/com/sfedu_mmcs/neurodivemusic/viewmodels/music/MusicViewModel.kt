package com.sfedu_mmcs.neurodivemusic.viewmodels.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfedu_mmcs.neurodivemusic.repositories.tracks.TrackRepository
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

var index = 0

@HiltViewModel
class MusicViewModel @Inject constructor(
        private val trackRepository: TrackRepository
) : ViewModel() {
    val trackQueue: MutableLiveData<MutableList<TrackData>> by lazy {
        MutableLiveData<MutableList<TrackData>>(mutableListOf())
    }

    val currentTrack: MutableLiveData<TrackData?> by lazy {
        MutableLiveData<TrackData?>(null)
    }

    val status: MutableLiveData<PlayStatus> = MutableLiveData(PlayStatus.Pause)

    fun next() {
        if (trackQueue.value?.isEmpty() == true) return;
        index = (index + 1) % trackQueue.value!!.size
        currentTrack.value = trackQueue.value!![index]
    }

    fun prev() {
        if (trackQueue.value?.isEmpty() == true) return
        index = (index + trackQueue.value!!.size - 1) % trackQueue.value!!.size
        currentTrack.value =  trackQueue.value!![index]
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
        trackQueue.value = trackRepository.getEntities().toMutableList()
        currentTrack.value = trackQueue.value!![index]
    }
}