package com.sfedu_mmcs.neurodivemusic.viewmodels.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {
    val genresList = mutableListOf<String>()
}