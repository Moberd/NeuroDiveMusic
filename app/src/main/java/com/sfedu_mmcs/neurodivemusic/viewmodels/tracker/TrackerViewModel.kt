package com.sfedu_mmcs.neurodivemusic.viewmodels.tracker

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.model.Emotion
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.model.TrackerInfo

open class TrackerViewModel : ViewModel() {
    val availableTrackers = MutableLiveData<List<TrackerInfo>>(listOf())

    val selectedTracker = MutableLiveData<TrackerInfo?>(null)
    val calibrated = MutableLiveData<Boolean>(false)

    val currentEmotion = MutableLiveData(Emotion.Neutral)
    val addTrackToFavorite = MutableLiveData(false)
    val skipTrack = MutableLiveData(false)

    var happyCount = 0
    var sadCount = 0

    private fun setCurrentEmotion() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (Math.random() > 0.5) {
                currentEmotion.value = Emotion.Neutral
            } else if (Math.random() > 0.5) {
                sadCount = 0
                happyCount += 1
                currentEmotion.value = Emotion.Happy
            } else {
                happyCount = 0
                sadCount += 1

                currentEmotion.value = if (Math.random() > 0.5) Emotion.Sad else Emotion.Angry
            }

            if (happyCount > 2) {
                Log.i("123.1", "too happy")
                happyCount = 0
                emitAddToFavorite()
            }

            if (sadCount > 2) {
                Log.i("123.1", "too sad")
                sadCount = 0
                emitSkipTrack()
            }

            Log.i("123.1", "${currentEmotion.value}")
            setCurrentEmotion()
        }, 1000)
    }

    private fun emitAddToFavorite() {
        addTrackToFavorite.value = true
        addTrackToFavorite.value = false
    }

    private fun emitSkipTrack() {
        skipTrack.value = true
        skipTrack.value = false
    }

    fun findTrackers() {
        Handler(Looper.getMainLooper()).postDelayed({
            availableTrackers.value =
                listOf(
                    TrackerInfo("123456", "Tracker 1"),
                    TrackerInfo("123457", "Tracker 2"),
                    TrackerInfo("123458", "Tracker 3"),
                    TrackerInfo("qwerty", "Tracker 4"),
                    TrackerInfo("qwertu", "Tracker 5"),
                    TrackerInfo("qwerti", "Tracker 6")
                )
        }, 1500)
    }

    fun calibrate() {
        Handler(Looper.getMainLooper()).postDelayed({
            calibrated.value = true
            setCurrentEmotion()
        }, 1500)
    }
}