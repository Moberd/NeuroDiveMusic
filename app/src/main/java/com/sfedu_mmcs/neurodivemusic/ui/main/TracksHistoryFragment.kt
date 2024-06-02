package com.sfedu_mmcs.neurodivemusic.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.FragmentTracksHistrotyBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.HistoryViewModel
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.model.HistoryTrackData


class TracksHistoryFragment : Fragment() {
    private lateinit var binding: FragmentTracksHistrotyBinding
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val historyListAdapter = TracksHistoryListAdapter {
        showDialog(it)
    }

    val historyListObserver = Observer<List<HistoryTrackData>> {
        historyListAdapter.setHistory(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTracksHistrotyBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        historyViewModel.historyList.observe(viewLifecycleOwner, historyListObserver)

        binding.tracksHistoryList.apply {
            adapter = historyListAdapter
            layoutManager = LinearLayoutManager(this@TracksHistoryFragment.context)
        }
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.tracks_history_list_divider
            )
        }!!)
        binding.tracksHistoryList.addItemDecoration(itemDecorator)
    }

    fun showDialog(trackData: HistoryTrackData) {
        val builder = AlertDialog.Builder(this.context)
        val customView = layoutInflater.inflate(R.layout.delete_track_dialog, null)
        builder.setView(customView)
        val dialog = builder.create()
        val posButton = customView.findViewById<Button>(R.id.tv2)
        posButton.setOnClickListener {
            historyListAdapter.deleteTrack(trackData)
            historyViewModel.deleteFromHistory(trackData)
            dialog.dismiss()
        }
        val negButton = customView.findViewById<Button>(R.id.tv1)
        negButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = TracksHistoryFragment()
    }
}