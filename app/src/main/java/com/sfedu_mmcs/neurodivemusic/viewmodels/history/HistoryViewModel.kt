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
                    id="Ic5YEw_QzGs",
                    artist="Chernikovskaya Hata",
                    name="Нажми на кнопку",
                    duration=10,
                    cover=R.drawable.logo.toDrawable()
                ),
                TrackData(
                    id="tUBVEKzsZ-k",
                    artist="Альянс",
                    name="На Заре",
                    duration=10,
                    cover=R.drawable.logo.toDrawable()
                ),
                TrackData(
                    id="HvZrAOIW5W8",
                    artist="Смысловые Галлюцинации",
                    name="Вечно молодой",
                    duration=10,
                    cover=R.drawable.logo.toDrawable()
                ),
            )
        }, 1000)
    }
}
