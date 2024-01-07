package com.sfedu_mmcs.neurodivemusic.viewmodels.tracker

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.model.TrackerInfo

class TrackerViewModel : ViewModel() {
    val availableTrackers: MutableLiveData<List<TrackerInfo>> by lazy {
        MutableLiveData<List<TrackerInfo>>(listOf())
    }
    val selectedTracker = MutableLiveData<TrackerInfo?>(null)
    val calibrated = MutableLiveData<Boolean>(false)

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
        }, 1500)
    }
}