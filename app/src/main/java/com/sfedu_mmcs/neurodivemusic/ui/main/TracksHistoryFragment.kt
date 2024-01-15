package com.sfedu_mmcs.neurodivemusic.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentTracksHistrotyBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.HistoryViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData

class TracksHistoryFragment : Fragment() {
    lateinit var binding: FragmentTracksHistrotyBinding
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val historyListAdapter = TracksHistoryListAdapter {
        val controller = findNavController()
        val action = TracksHistoryFragmentDirections.actionTracksHistoryFragmentToTrackInfoFragment(it)
        controller.navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyViewModel.fetchHistory()
        historyViewModel.historyList.observe(activity as LifecycleOwner) {
            if (it == null) return@observe

            historyListAdapter.setHistory(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTracksHistrotyBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tracksHistoryList.apply {
            adapter = historyListAdapter
            layoutManager = LinearLayoutManager(this@TracksHistoryFragment.context)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = TracksHistoryFragment()
    }
}