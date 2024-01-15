package com.sfedu_mmcs.neurodivemusic.viewmodels.music.model

import android.graphics.drawable.Drawable

data class TrackData(
    val id: String,
    val artist: String,
    val name: String,
    val duration: Number,
    val cover: Drawable
)
