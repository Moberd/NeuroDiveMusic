package com.sfedu_mmcs.neurodivemusic.viewmodels.history

import android.os.Handler
import android.os.Looper
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData

class HistoryViewModel : ViewModel() {
    val historyList = MutableLiveData<List<TrackData>?>(null)

    fun fetchHistory() {
        Handler(Looper.getMainLooper()).postDelayed({
            historyList.value = listOf<TrackData>(
                TrackData(
                    "123456",
                    "first artist",
                    "some long track name",
                    10,
                    R.drawable.logo.toDrawable()
                ),
                TrackData(
                    "123457",
                    "second artist",
                    "some long track name",
                    10,
                    R.drawable.button_gradient.toDrawable()
                ),
                TrackData(
                    "123458",
                    "third artist",
                    "some long track name",
                    10,
                    R.drawable.logo.toDrawable()
                )
            )
        }, 1500)
    }
}
