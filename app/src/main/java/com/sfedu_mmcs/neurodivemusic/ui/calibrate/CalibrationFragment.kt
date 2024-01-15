package com.sfedu_mmcs.neurodivemusic.ui.calibrate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentCalibrationBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.TrackerViewModel

class CalibrationFragment : Fragment() {
    private lateinit var binding: FragmentCalibrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalibrationBinding.inflate(inflater)

        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = CalibrationFragment()
    }
}