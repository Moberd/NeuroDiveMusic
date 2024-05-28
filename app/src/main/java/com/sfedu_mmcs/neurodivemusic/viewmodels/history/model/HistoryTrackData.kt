package com.sfedu_mmcs.neurodivemusic.viewmodels.history.model

import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.model.Emotion

data class HistoryTrackData @JvmOverloads constructor(
    val id: String = "",
    val artist: String = "",
    val name: String = "",
    val emotions: List<Emotion> = listOf(),
    val isFavorite: Boolean = false,
)