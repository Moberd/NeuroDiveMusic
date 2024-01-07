package com.sfedu_mmcs.neurodivemusic.ui.calibrate

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.databinding.TrackerListItemBinding
import com.sfedu_mmcs.neurodivemusic.viewmodels.tracker.model.TrackerInfo

class TrackersListAdapter(private val setSelectedItem: (TrackerInfo) -> Unit) :
    RecyclerView.Adapter<TrackersListAdapter.ViewHolder>() {
    private var trackersList: List<TrackerInfo> = listOf()
    private var selectedItem: TrackerInfo? = null

    class ViewHolder(
        view: View,
        val setSelectedItem: (TrackerInfo) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val binding = TrackerListItemBinding.bind(view)
        fun bind(tracker: TrackerInfo, isSelected: Boolean) = with(binding) {
            trackerID.text = tracker.id
            trackerName.text = tracker.name
            trackerCard.setOnClickListener {
                setSelectedItem(tracker)
            }
            trackerCard.isSelected = isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.tracker_list_item, parent, false)

        return ViewHolder(view, setSelectedItem)

    }

    override fun getItemCount(): Int = trackersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = trackersList[position]
        holder.bind(item, selectedItem?.id == item.id)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<TrackerInfo>) {
        trackersList = newList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedItem(item: TrackerInfo) {
        selectedItem = item
        notifyDataSetChanged()
    }
}