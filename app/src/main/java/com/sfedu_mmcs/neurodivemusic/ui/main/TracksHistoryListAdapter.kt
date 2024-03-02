package com.sfedu_mmcs.neurodivemusic.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sfedu_mmcs.neurodivemusic.databinding.TracksHistoryListItemBinding
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import com.sfedu_mmcs.neurodivemusic.R
import com.sfedu_mmcs.neurodivemusic.constants.getCoverUrl
import com.sfedu_mmcs.neurodivemusic.viewmodels.history.model.HistoryTrackData

@Throws(IOException::class)
fun drawableFromUrl(url: String?): Drawable? {
    val x: Bitmap
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connect()
    val input = connection.inputStream
    x = BitmapFactory.decodeStream(input)
    return BitmapDrawable(Resources.getSystem(), x)
}

class TracksHistoryListAdapter(
    val onTrackClick: (id: String) -> Unit
) :
    RecyclerView.Adapter<TracksHistoryListAdapter.ViewHolder>() {
    var tracksList = listOf<HistoryTrackData>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TracksHistoryListItemBinding.bind(view)

        fun bind(track: HistoryTrackData, onTrackClick: (id: String) -> Unit) = with(binding) {
            Glide.with(itemView)
                .load(getCoverUrl(track.id))
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.logo.toDrawable()) // Placeholder image
                        .error(R.drawable.logo.toDrawable()) // Error image in case of loading failure
                )
                .into(trackCover)

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
    fun setHistory(newList: List<HistoryTrackData>) {
        tracksList = newList
        notifyDataSetChanged()
    }
}