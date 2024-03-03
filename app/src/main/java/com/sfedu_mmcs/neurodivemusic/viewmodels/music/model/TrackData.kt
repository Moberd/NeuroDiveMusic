package com.sfedu_mmcs.neurodivemusic.viewmodels.music.model

import android.graphics.drawable.Drawable

data class TrackData @JvmOverloads constructor(
    val id: String = "",
    val artist: String = "",
    val name: String = "",
    val isFavorite: Boolean = false,
)