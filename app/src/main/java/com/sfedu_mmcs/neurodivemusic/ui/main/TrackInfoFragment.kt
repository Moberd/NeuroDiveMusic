package com.sfedu_mmcs.neurodivemusic.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentTrackInfoBinding

class TrackInfoFragment : Fragment() {
    lateinit var binding: FragmentTrackInfoBinding
    val args: TrackInfoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trackId.text =
            String.format(resources.getString(R.string.main_track_info_text_placeholder), args.trackId)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrackInfoFragment()
    }
}