package com.sfedu_mmcs.neurodivemusic.ui.calibrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentTrackerSelectBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.TrackerViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.model.TrackerInfo

class TrackerSelectFragment : Fragment() {
    private lateinit var binding: FragmentTrackerSelectBinding
    private val trackerViewModel: TrackerViewModel by activityViewModels()
    private val trackerListAdapter = TrackersListAdapter {
        trackerViewModel.selectedTracker.value = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        trackerViewModel.findTrackers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackerSelectBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = findNavController()

        trackerViewModel.availableTrackers.observe(viewLifecycleOwner) {
            trackerListAdapter.setList(it)
        }

        trackerViewModel.selectedTracker.observe(viewLifecycleOwner) {
            if (it !is TrackerInfo) return@observe

            trackerListAdapter.setSelectedItem(it)
        }

        with(binding) {
            trackersList.apply {
                adapter = trackerListAdapter
                layoutManager = LinearLayoutManager(this@TrackerSelectFragment.context)
            }

            calibrateButton.setOnClickListener {
                val action =
                    TrackerSelectFragmentDirections.actionTrackerSelectFragmentToCalibrationFragment()
                controller.navigate(action)
                trackerViewModel.calibrate()
            }
        }


        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.tracks_history_list_divider
            )
        }!!)

        binding.trackersList.addItemDecoration(itemDecorator)

    }

    companion object {
        @JvmStatic
        fun newInstance() = TrackerSelectFragment()
    }
}