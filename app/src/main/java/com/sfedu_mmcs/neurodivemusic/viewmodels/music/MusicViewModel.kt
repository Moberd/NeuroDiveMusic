package com.sfedu_mmcs.neurodivemusic.viewmodels.music

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
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
    private var trackQueue = mutableListOf<TrackData>()
    private var trackQueueIndex = 0

    val currentTrack = MutableLiveData<TrackData?>(null)

    val youTubePlayer = MutableLiveData<YouTubePlayer>()

    val currentSecond = MutableLiveData<Int>(0)
    val duration = MutableLiveData<Int>(0)

    val status = MutableLiveData(PlayStatus.Pause)

    val trackChange = MutableLiveData<Pair<TrackData?, TrackData>?>()

    fun next() {
        Log.i("123", "next start $trackQueueIndex")
        Log.i("123", "next start ${trackQueue.size}")
        val nextTrack =
            if (trackQueueIndex < trackQueue.size - 1) trackQueue[trackQueueIndex + 1]
            else trackRepository.getNextTrack()

        trackQueueIndex += 1

        trackChange.value = Pair(currentTrack.value, nextTrack)

        currentTrack.value = nextTrack
        trackQueue.add(nextTrack)

        Log.i("123", "next end $trackQueueIndex")
    }

    fun prev() {
        if (trackQueueIndex < 1) return

        val prevTrack = trackQueue[trackQueueIndex - 1]

        trackChange.value = Pair(currentTrack.value, prevTrack)
        currentTrack.value = prevTrack

        trackQueueIndex -= 1
        Log.i("123", "prev end $trackQueueIndex")
    }

    fun addCurrentTrackToFavorites() =
        currentTrack.value?.let {
            trackRepository.addToFavorites(it.id)

            trackQueue = trackQueue.map { track ->
                if (track.id == it.id) track.copy(isFavorite = true) else track
            }.toMutableList()

            currentTrack.value = it.copy(isFavorite = true)
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
        next()
    }
}