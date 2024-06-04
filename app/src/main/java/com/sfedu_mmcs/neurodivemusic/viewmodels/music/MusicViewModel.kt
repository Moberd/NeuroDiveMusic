package com.sfedu_mmcs.neurodivemusic.viewmodels.music

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.sfedu_mmcs.neurodivemusic.repositories.tracks.TrackRepository
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.PlayStatus
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData
import com.sfedu_mmcs.neurodivemusic.viewmodels.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

var index = 0

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val trackRepository: TrackRepository,
//    private val settingsViewModel: SettingsViewModel,
) : ViewModel() {
    private var trackQueue = mutableListOf<TrackData>()
    private var trackQueueIndex = 0

    val currentTrack = MutableLiveData<TrackData?>(null)

    val favorites = MutableLiveData<List<TrackData>>(emptyList())

    val youTubePlayer = MutableLiveData<YouTubePlayer>()

    val currentSecond = MutableLiveData<Int>(0)
    val duration = MutableLiveData<Int>(0)

    val status = MutableLiveData(PlayStatus.Pause)

    val trackChange = MutableLiveData<Pair<TrackData?, TrackData>?>()

    fun next(genres: List<String>? = null) {
        Log.i("123", "next start $trackQueueIndex")
        Log.i("123", "next start ${trackQueue.size}")

        val nextTrack =
            if (trackQueueIndex < trackQueue.size - 1) trackQueue[trackQueueIndex + 1]
            else trackRepository.getNextTrack(genres)

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

    fun playTrack(id: String) {
        val nextTack = trackRepository.getTrack(id) ?: return

        trackChange.value = Pair(currentTrack.value, nextTack)
        currentTrack.value = nextTack
        setPlay()

        trackQueue.clear()
        trackQueue.add(nextTack)
    }

    fun addCurrentTrackToFavorites() =
        currentTrack.value?.let {
            Log.i("123.Like.addCurrentTrackToFavorites", "addCurrentTrackToFavorites")
            trackRepository.addToFavorites(it.id)

            favorites.value = trackRepository.fetchFavorites()

            trackQueue = trackQueue.map { track ->
                if (track.id == it.id) track.copy(isFavorite = true) else track
            }.toMutableList()

            currentTrack.value = it.copy(isFavorite = true)
        }

    fun removeFromFavorites(id: String) {
        trackRepository.deleteFromFavorites(id)

        trackQueue = trackQueue.map { track ->
            if (track.id == id) track.copy(isFavorite = false) else track
        }.toMutableList()

        currentTrack.value = currentTrack.value?.copy(isFavorite = false)

        favorites.value = trackRepository.fetchFavorites()
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
        favorites.value = trackRepository.fetchFavorites()
    }
}