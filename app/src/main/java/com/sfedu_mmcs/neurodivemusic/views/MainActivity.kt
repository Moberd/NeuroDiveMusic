package com.sfedu_mmcs.neurodivemusic.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sfedu_mmcs.neurodivemusic.R

class MainActivity : AppCompatActivity() {
    private lateinit var calibrateBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calibrateBtn = findViewById(R.id.calibrate_btn)
        calibrateBtn.setOnClickListener {
            val intent = Intent(this, CalibrateActivity::class.java)
            startActivity(intent);
        }
    }
}