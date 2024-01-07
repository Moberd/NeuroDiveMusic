package com.sfedu_mmcs.neurodivemusic.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.TracksHistoryListItemBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.music.model.TrackData

class TracksHistoryListAdapter(val onTrackClick: (id: String) -> Unit) :
    RecyclerView.Adapter<TracksHistoryListAdapter.ViewHolder>() {
    var tracksList = listOf<TrackData>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TracksHistoryListItemBinding.bind(view)

        fun bind(track: TrackData, onTrackClick: (id: String) -> Unit) = with(binding) {
            trackCover.setImageDrawable(track.cover)
            artist.text = track.artist
            trackName.text = track.name
            card.setOnClickListener { onTrackClick(track.id) }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tracks_history_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tracksList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tracksList[position], onTrackClick)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setHistory(newList: List<TrackData>) {
        tracksList = newList
        notifyDataSetChanged()
    }
}