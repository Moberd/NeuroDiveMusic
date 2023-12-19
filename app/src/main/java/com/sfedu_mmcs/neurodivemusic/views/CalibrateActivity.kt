package com.sfedu_mmcs.neurodivemusic.views

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.sfedu_mmcs.neurodivemusic.R

class CalibrateActivity: AppCompatActivity() {
    private lateinit var backBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibrate)
        backBtn = findViewById(R.id.calib_back)
        backBtn.setOnClickListener {
            finish()
        }
    }
}