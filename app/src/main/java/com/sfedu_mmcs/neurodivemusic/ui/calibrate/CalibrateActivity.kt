package com.sfedu_mmcs.neurodivemusic.ui.calibrate

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sfedu_mmcs.neurodivemusic.databinding.ActivityCalibrateBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.TrackerViewModel

class CalibrateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalibrateBinding
    private val trackerViewModel: TrackerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalibrateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackerViewModel.calibrated.observe(this) {
            if (!it) return@observe

            finish()
        }
    }
}